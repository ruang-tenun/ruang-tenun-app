package com.ruangtenun.app.view.main.product

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruangtenun.app.R
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.data.remote.response.ProductsItem
import com.ruangtenun.app.databinding.FragmentProductBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.main.AdapterProduct
import com.ruangtenun.app.viewmodel.main.MainViewModel

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterProduct: AdapterProduct
    private lateinit var catalogAdapter: ArrayAdapter<String>
    private val catalogOptions = mutableListOf<CatalogItem>()

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NywibmFtZSI6InNhZWZ1bCIsImVtYWlsIjoic2FlZnVsQGdtYWlsLmNvbSIsImlhdCI6MTczNDAxNDkzMSwiZXhwIjoxNzM0MDE4NTMxfQ.OtWCCrSUGgUbTpUX0IgJEp6p_LfXORVNmPT1zROm6XE"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupAdapter()
        setupCatalogDropdown()
        observeViewModel()

        binding.apply {
            searchView.setupWithSearchBar(binding.searchBar)

            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val keyword = searchView.text.toString()
                mainViewModel.searchProducts(keyword)
                searchView.hide()
                true
            }
        }

        mainViewModel.fetchCatalogs(token)
        mainViewModel.fetchProducts(token)

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun setupAdapter() {
        adapterProduct = AdapterProduct { productId ->
            val bundle = Bundle().apply {
                productId?.let { putInt("productId", it) }
            }
            findNavController().navigate(R.id.navigation_detail, bundle)
        }
        binding.rvProducts.adapter = adapterProduct
    }

    private fun setupCatalogDropdown() {
        catalogAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
        binding.catalogDropdown.setAdapter(catalogAdapter)

        binding.catalogDropdown.setOnClickListener {
            if (catalogAdapter.isEmpty) {
                mainViewModel.fetchCatalogs(token)
            }
        }

        binding.catalogDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedCatalog = if (position == 0) null else catalogOptions[position - 1].name
            mainViewModel.filterProductsByCatalog(selectedCatalog)
        }
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
                    val catalogNames = listOf("All") + catalogOptions.map { it.name.orEmpty() }
                    catalogAdapter.clear()
                    catalogAdapter.addAll(catalogNames)
                    catalogAdapter.notifyDataSetChanged()
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), result.error)
                }
            }
        }

        mainViewModel.productState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    setProducts(result.data)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), result.error)
                }
            }
        }
    }

    private fun setProducts(products: List<ProductsItem?>?) {
        adapterProduct.submitList(products.orEmpty())
        if (products.isNullOrEmpty()) {
            binding.tvEmptyProducts.visibility = View.VISIBLE
        } else {
            binding.tvEmptyProducts.visibility = View.GONE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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


