package com.ruangtenun.app.view.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentHomeBinding
import com.ruangtenun.app.utils.ToastUtils.showToast
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()

        return _binding!!.root
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                LOCATION_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            requestPermissionLauncher.launch(LOCATION_PERMISSION)
        }
    }

    private fun getCurrentLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    showToast(requireContext(), getString(R.string.location_acquired))
                    getCityName(latitude, longitude)
                } else {
                    showToast(requireContext(), getString(R.string.location_failed))
                }
            }.addOnFailureListener {
                showToast(requireContext(), getString(R.string.location_failed))
            }
        } catch (e: SecurityException) {
            showToast(requireContext(), getString(R.string.location_denied))
        }
    }

    private fun getCityName(lat: Double, lon: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val cityName = addresses[0].locality
                val countryName = addresses[0].countryName
                val formattedMessage =
                    requireContext().getString(R.string.location_city_found, cityName, countryName)
                showToast(requireContext(), formattedMessage)
            } else {
                showToast(requireContext(), getString(R.string.location_address_not_found))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}