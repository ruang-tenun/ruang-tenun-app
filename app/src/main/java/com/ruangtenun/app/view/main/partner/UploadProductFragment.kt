package com.ruangtenun.app.view.main.partner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentUploadProductBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.utils.reduceFileImage
import com.ruangtenun.app.utils.uriToFile
import com.ruangtenun.app.view.main.camera.CameraActivity
import com.ruangtenun.app.view.main.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.ruangtenun.app.view.main.maps.MapsActivity
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.main.MainViewModel
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Locale
import kotlin.getValue

class UploadProductFragment : Fragment() {

    private var _binding: FragmentUploadProductBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    private var latitude: Double? = null
    private var longitude: Double? = null

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val mainViewModel: MainViewModel by viewModels {
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
            UCrop.of(uri, destinationUri)
                .withMaxResultSize(1080, 1080)
                .getIntent(requireContext()).let { cropIntent ->
                    cropResultLauncher.launch(cropIntent)
                }
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
                UCrop.of(imageUri, destinationUri)
                    .withMaxResultSize(1080, 1080)
                    .getIntent(requireContext()).let { cropIntent ->
                        cropResultLauncher.launch(cropIntent)
                    }
            }
        }
    }

    private val mapsResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val latitude = result.data?.getDoubleExtra("latitude", 0.0)
            val longitude = result.data?.getDoubleExtra("longitude", 0.0)

            if (latitude != null && longitude != null) {
                showAddress(longitude, latitude)
            } else {
                showToast(requireContext(), getString(R.string.location_error))
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
        _binding = FragmentUploadProductBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }

        binding.apply {
            btnUploadImage.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCameraX() }
            btnNavigateToMap.setOnClickListener {
                navigateToMapsActivity()
            }
            btnUpload.setOnClickListener { addProduct() }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
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

    private fun addProduct() {

        binding.apply {
            tfProductName.text?.isBlank() ?: showToast(
                requireContext(),
                "Product name cannot be empty"
            )

            tfLinkProduct.text?.isBlank() ?: showToast(
                requireContext(),
                "E-commerce URL cannot be empty"
            )

            tfEcommerce.text?.isBlank() ?: showToast(
                requireContext(),
                "Ecommerce Name cannot be empty"
            )

            tfAddress.text?.isBlank() ?: showToast(
                requireContext(),
                "Address cannot be empty"
            )
        }

        if (latitude == null || longitude == null) {
            showToast(requireContext(), "Please select a valid location")
            return
        }

        currentImageUri?.let { uri ->

            val token = authViewModel.getSession().value?.token ?: ""

            val imageFile = uriToFile(uri, requireContext()).reduceFileImage()

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            val name = binding.tfProductName.text.toString()
            val ecommerceName = binding.tfEcommerce.text.toString()
            val ecommerceUrl = binding.tfLinkProduct.text.toString()
            val address = binding.tfAddress.text.toString()

            if (latitude == null || longitude == null) {
                showToast(requireContext(), getString(R.string.location_error))
                return
            }

            mainViewModel.addProduct(
                token,
                multipartBody,
                name,
                address,
                latitude!!.toString(),
                longitude!!.toString(),
                1,
                6,
                ecommerceName,
                ecommerceUrl
            ).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(requireContext(), result.error)
                    }

                    ResultState.Idle -> showLoading(true)
                    ResultState.Loading -> showLoading(true)
                    is ResultState.Success -> {
                        showLoading(false)
                        showToast(requireContext(), "Upload Successfully")
                        findNavController().popBackStack()
                    }
                }
            }
        } ?: showToast(requireContext(), getString(R.string.no_image_selected))
    }

    private fun navigateToMapsActivity() {
        val intent = Intent(requireContext(), MapsActivity::class.java)
        mapsResultLauncher.launch(intent)
    }

    private fun showAddress(longitude: Double, latitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 1)
        if (!address.isNullOrEmpty()) {
            val fullAddress = address[0].getAddressLine(0)
            binding.tfLocation.setText(fullAddress)
        } else {
            binding.tfLocation.setText(getString(R.string.location_error))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}
