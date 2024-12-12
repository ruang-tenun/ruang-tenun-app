package com.ruangtenun.app.view.main.partner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentUploadProductBinding
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.view.main.camera.CameraActivity
import com.ruangtenun.app.view.main.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.ruangtenun.app.view.main.maps.MapsActivity
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.Locale

class UploadProductFragment : Fragment() {

    private var _binding: FragmentUploadProductBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

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

    private fun navigateToMapsActivity() {
        val intent = Intent(requireContext(), MapsActivity::class.java)
        mapsResultLauncher.launch(intent)
    }

    private fun showAddress(longitude: Double, latitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 1)
        if (!address.isNullOrEmpty()) {
            val fullAddress = address[0].getAddressLine(0)
            binding.tfLocation.setText(fullAddress)
        } else {
            binding.tfLocation.setText(getString(R.string.location_error))
        }
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }
}
