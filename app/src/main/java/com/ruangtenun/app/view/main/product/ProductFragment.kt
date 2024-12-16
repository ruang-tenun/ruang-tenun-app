package com.ruangtenun.app.view.main.product

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ruangtenun.app.R
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.databinding.FragmentProductBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.favorite.FavoriteViewModel
import com.ruangtenun.app.viewmodel.main.AdapterProduct
import com.ruangtenun.app.viewmodel.main.MainViewModel

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterProduct: AdapterProduct
    private val catalogOptions = mutableListOf<CatalogItem>()

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var token = ""
    private var userId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
            token = user.token
            userId = user.id
            mainViewModel.fetchCatalogs(token)
            mainViewModel.fetchProducts(token)
            favoriteViewModel.getFavoriteByUserId(token, userId)
        }

        setupRecyclerView()
        setupAdapter()
        observeViewModel()

        binding.apply {
            searchView.setupWithSearchBar(binding.searchBar)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                mainViewModel.searchProducts(keyword)
                swipeRefreshLayout.setOnRefreshListener {
                    mainViewModel.fetchProducts(token)
                    swipeRefreshLayout.isRefreshing = false
                }
                searchView.hide()
                true
            }

            btnFilter.setOnClickListener {
                showCatalogFilterDialog()
            }

            swipeRefreshLayout.setOnRefreshListener {
                mainViewModel.fetchProducts(token)
                swipeRefreshLayout.isRefreshing = false
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setupAdapter() {
        adapterProduct = AdapterProduct(
            onItemClick = { productId ->
                val bundle = Bundle().apply { putInt("productId", productId ?: 0) }
                findNavController().navigate(R.id.navigation_detail, bundle)
            },
            onFavoriteClick = { productId, isFavorite ->
                adapterProduct.updateFavoriteStatus(productId ?: 0, isFavorite)

                if (isFavorite) {
                    favoriteViewModel.addFavorite("Bearer $token", userId, productId ?: 0)
                        .observe(viewLifecycleOwner) { result ->
                            when (result) {
                                is ResultState.Success -> {
                                    showToast(
                                        requireContext(),
                                        getString(R.string.success_add_favorite)
                                    )
                                }

                                is ResultState.Error -> {
                                    adapterProduct.updateFavoriteStatus(productId ?: 0, false)
                                    showToast(
                                        requireContext(),
                                        getString(R.string.error_add_favorite)
                                    )
                                }

                                ResultState.Idle -> {
                                }

                                ResultState.Loading -> {
                                }
                            }
                        }
                } else {
                    val favoriteId = adapterProduct.getFavoriteIdByProductId(productId ?: 0)
                    if (favoriteId != null) {
                        favoriteViewModel.removeFavorite("Bearer $token", favoriteId)
                            .observe(viewLifecycleOwner) { result ->
                                when (result) {
                                    is ResultState.Success -> {
                                        showToast(
                                            requireContext(),
                                            getString(R.string.success_remove_favorite)
                                        )
                                    }

                                    is ResultState.Error -> {
                                        adapterProduct.updateFavoriteStatus(productId ?: 0, true)
                                        showToast(
                                            requireContext(),
                                            getString(R.string.error_remove_favorite)
                                        )
                                    }

                                    ResultState.Idle -> {
                                    }

                                    ResultState.Loading -> {
                                    }
                                }
                            }
                    } else {
//                        showToast(requireContext(), getString(R.string.error_favorite_not_found))
                    }
                }
            }
        )
        binding.rvProducts.adapter = adapterProduct
    }

    private fun observeViewModel() {
        mainViewModel.catalogState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    catalogOptions.clear()
                    catalogOptions.addAll(result.data)
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), result.error)
                }
            }
        }

        mainViewModel.productState.observe(viewLifecycleOwner) { productResult ->
            when (productResult) {
                is ResultState.Success -> {
                    showLoading(false)
                    setProducts(productResult.data)
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), productResult.error)
                }

                else -> {}
            }
        }

        favoriteViewModel.favoriteByUserId.observe(viewLifecycleOwner) { favoriteResult ->
            when (favoriteResult) {
                is ResultState.Success -> {
                    adapterProduct.setFavorites(favoriteResult.data)
                }

                is ResultState.Error -> {
                    showToast(requireContext(), favoriteResult.error)
                }

                is ResultState.Loading -> {
                }

                else -> {}
            }
        }
    }

    private fun setProducts(products: List<ProductsItem?>?) {
        adapterProduct.submitList(products.orEmpty())
        binding.tvEmptyProducts.visibility =
            if (products.isNullOrEmpty()) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showCatalogFilterDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_categories_dropdown, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val spinnerCategories = dialogView.findViewById<Spinner>(R.id.spinnerCategories)
        val btnCloseDialog = dialogView.findViewById<Button>(R.id.btnCloseDialog)

        val catalogNames = listOf("All") + catalogOptions.map { it.name.orEmpty() }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            catalogNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = adapter

        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory =
                    if (position == 0) null else catalogOptions[position - 1].name
                mainViewModel.filterByCatalog(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchCatalogs(token)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}