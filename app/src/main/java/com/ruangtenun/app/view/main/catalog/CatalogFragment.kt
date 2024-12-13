package com.ruangtenun.app.view.main.catalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruangtenun.app.R
import com.ruangtenun.app.data.remote.response.CatalogItem
import com.ruangtenun.app.databinding.FragmentCatalogBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.catalog.AdapterCatalog
import com.ruangtenun.app.viewmodel.main.MainViewModel

class CatalogFragment : Fragment() {

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterCatalog: AdapterCatalog

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var token: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)

        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
            token = user.token
            mainViewModel.fetchCatalogs(token)
        }

        setupRecyclerView()
        setupAdapter()
        observeCatalogs()

        return _binding!!.root
    }

    private fun setupRecyclerView() {
        binding.rvCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun setupAdapter() {
        adapterCatalog = AdapterCatalog { catalogId ->
            val bundle = Bundle().apply {
                catalogId?.let { putInt("catalogId", it) }
            }
            findNavController().navigate(R.id.navigation_detail_catalog, bundle)
        }
        binding.rvCatalog.adapter = adapterCatalog
    }


    private fun observeCatalogs() {
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
        adapterCatalog.submitList(products)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}