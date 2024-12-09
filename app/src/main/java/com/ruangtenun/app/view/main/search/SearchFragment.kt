package com.ruangtenun.app.view.main.search

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruangtenun.app.utils.ToastUtils.showToast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.ruangtenun.app.R
import com.ruangtenun.app.data.model.ClassificationHistory
import com.ruangtenun.app.data.remote.api.ApiConfig
import com.ruangtenun.app.data.remote.response.FileUploadResponse
import com.ruangtenun.app.data.repository.HistoryRepository
import com.ruangtenun.app.databinding.FragmentSearchBinding
import com.ruangtenun.app.utils.FileUtils
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.utils.reduceFileImage
import com.ruangtenun.app.utils.uriToFile
import com.ruangtenun.app.view.main.camera.CameraActivity
import com.ruangtenun.app.view.main.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.ruangtenun.app.viewmodel.history.HistoryViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val cropResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val resultUri = UCrop.getOutput(result.data!!)
            if (resultUri != null) {
                currentImageUri = resultUri
                showImage()
            } else {
                showToast(requireContext(), getString(R.string.crop_failed))
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            showToast(requireContext(), getString(R.string.crop_canceled))
        } else {
            val cropError = UCrop.getError(result.data!!)
            showToast(requireContext(), getString(R.string.crop_failed) + cropError?.message)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val destinationUri = Uri.fromFile(
                File(
                    requireContext().cacheDir,
                    "cropped_image_${System.currentTimeMillis()}.jpg"
                )
            )
            val uCropIntent = UCrop.of(uri, destinationUri)
                .withMaxResultSize(1080, 1080)
                .getIntent(requireContext())

            cropResultLauncher.launch(uCropIntent)
        } else {
            showToast(requireContext(), getString(R.string.no_image_selected))
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val imageUri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (imageUri != null) {
                val destinationUri = Uri.fromFile(
                    File(
                        requireContext().cacheDir,
                        "cropped_image_${System.currentTimeMillis()}.jpg"
                    )
                )
                val uCropIntent = UCrop.of(imageUri, destinationUri)
                    .withMaxResultSize(1080, 1080)
                    .getIntent(requireContext())

                cropResultLauncher.launch(uCropIntent)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast(requireContext(), getString(R.string.permission_granted))
            } else {
                showToast(requireContext(), getString(R.string.permission_denied))
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }

        binding.apply {
            btnUploadImage.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCameraX() }
            btnAnalyze.setOnClickListener { uploadImage() }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return _binding!!.root
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImage.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val filename = "image_${System.currentTimeMillis()}.jpg"
            val savedImagePath = FileUtils.saveImageToInternalStorage(requireContext(), uri, filename)

            if (savedImagePath != null) {
                val imageFile = File(savedImagePath).reduceFileImage()
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestImageFile
                )

                lifecycleScope.launch {
                    try {
                        val apiService = ApiConfig.getPredictService()
                        val successResponse = apiService.uploadImage(multipartBody)

                        successResponse.result?.let { predictionResult ->
                            val history = ClassificationHistory(
                                weavingName = predictionResult,
                                confidenceScore = 0.95,
                                imageUrl = savedImagePath,
                                createdAt = System.currentTimeMillis().toString()
                            )
                            historyViewModel.savePhotoAndHistory(history)

                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Hasil Prediksi")
                                .setMessage(predictionResult)
                                .setPositiveButton("Close") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                        showToast(requireContext(), "Error: ${errorResponse.result}")
                    } catch (e: Exception) {
                        showToast(requireContext(), "Terjadi kesalahan, coba lagi.")
                    }
                }
            } else {
                showToast(requireContext(), getString(R.string.image_save_failed))
            }
        } ?: showToast(requireContext(), getString(R.string.empty_image_warning))
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun saveImageToInternalStorage(uri: Uri, filename: String): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, filename)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



