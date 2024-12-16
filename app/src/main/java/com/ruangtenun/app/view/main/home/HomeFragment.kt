package com.ruangtenun.app.view.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ruangtenun.app.R
import com.ruangtenun.app.data.local.pref.UserModel
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.databinding.FragmentHomeBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.SpaceItemDecoration
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.catalog.AdapterCatalog
import com.ruangtenun.app.viewmodel.main.AdapterProduct
import com.ruangtenun.app.viewmodel.main.MainViewModel
import java.util.Locale
import kotlin.getValue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var token: String = ""

    private lateinit var productAdapter: AdapterProduct
    private lateinit var adapterCatalog: AdapterCatalog
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

        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
            token = user.token
            displayUserData(user)
            mainViewModel.fetchProducts(token)
            mainViewModel.fetchCatalogs(token)
        }

        setupRecyclerView()
        observeViewModel()

        return _binding!!.root
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                LOCATION_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showToast(requireContext(), getString(R.string.permission_granted))
        } else {
            requestPermissionLauncher.launch(LOCATION_PERMISSION)
        }
    }

    private fun getCurrentLocation(onLocationRetrieved: (Location) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    getCityName(location.latitude, location.longitude)
                    onLocationRetrieved(location)
                } else {
                    showToast(requireContext(), getString(R.string.location_failed))
                }
            }.addOnFailureListener {
                showToast(requireContext(), getString(R.string.location_failed))
            }
        } catch (_: SecurityException) {
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

    private fun setupRecyclerView() {
        productAdapter = AdapterProduct()
        adapterCatalog = AdapterCatalog()
        binding.rvNear.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = productAdapter
        }
        binding.rvCatalog.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCatalog
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterCatalog
            addItemDecoration(SpaceItemDecoration(50))
        }
    }

    private fun setProducts(products: List<ProductsItem>, currentLocation: Location) {
        val nearbyProducts = products.filter { product ->
            product.latitude != null && product.longitude != null &&
                    calculateDistance(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        product.latitude,
                        product.longitude
                    ) <= 10000
        }

        if (nearbyProducts.isNotEmpty()) {
            productAdapter.submitList(nearbyProducts)
            binding.tvEmptyProducts.visibility = View.GONE
        } else {
            binding.tvEmptyProducts.visibility = View.VISIBLE
            showToast(requireContext(), getString(R.string.no_products_nearby))
            val closestProducts = products.mapNotNull { product ->
                if (product.latitude != null && product.longitude != null) {
                    val distance = calculateDistance(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        product.latitude,
                        product.longitude
                    )
                    Pair(product, distance)
                } else {
                    null
                }
            }.sortedBy { it.second }

            val topClosestProducts = closestProducts.take(5).map { it.first }
            productAdapter.submitList(topClosestProducts)
        }
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    private fun displayUserData(user: UserModel?) {
        if (user != null) {
            binding.tvUsername.text = getString(R.string.username, user.name)
        }
    }

    private fun observeViewModel() {
        mainViewModel.productState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Idle -> {
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    getCurrentLocation { currentLocation ->
                        setProducts(state.data, currentLocation)
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), state.error)
                }
            }
        }

        mainViewModel.catalogState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    setCatalog(result.data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), result.error)
                }
            }
        }
    }

    private fun setCatalog(products: List<CatalogItem?>?) {
        val limitedProducts = products?.take(3)
        adapterCatalog.submitList(limitedProducts)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}