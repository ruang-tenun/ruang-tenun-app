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
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_accept),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_denied),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationPermission()

        return binding.root
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
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_acquired),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Location", "Latitude: $latitude, Longitude: $longitude")
                    getCityName(latitude, longitude)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.location_failed),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.location_failed),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                requireContext(),
                getString(R.string.location_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getCityName(lat: Double, lon: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val cityName = addresses[0].locality
                val countryName = addresses[0].countryName
                Log.d("City", "City: $cityName, Country: $countryName")
                Toast.makeText(
                    requireContext(),
                    "You're in $cityName, $countryName",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e("Geocoder", "No address found for location")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Geocoder", "Error: ${e.message}")
        }
    }

    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }
}