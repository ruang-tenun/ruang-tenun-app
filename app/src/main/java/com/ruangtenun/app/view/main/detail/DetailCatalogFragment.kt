package com.ruangtenun.app.view.main.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentDetailCatalogBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.main.MainViewModel

class DetailCatalogFragment : Fragment() {

    private var _binding: FragmentDetailCatalogBinding? = null
    private val binding get() = _binding!!

    private var catalogId: Int? = null

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
        _binding = FragmentDetailCatalogBinding.inflate(layoutInflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
            token = user.token
            mainViewModel.fetchCatalogDetail(token, catalogId!!)
        }

        catalogId = arguments?.getInt("catalogId")

        mainViewModel.catalogDetailState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }

                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    val catalog = result.data
                    binding.apply {
                        tvProductName.append("\n${catalog.name}")
                        tvDescription.append("\n${catalog.description}")
                        tvAddress.append("\n${catalog.address}")
                        Glide.with(requireContext())
                            .load(catalog.imageUrl)
                            .placeholder(R.drawable.ic_place_holder)
                            .into(previewImage)
                    }
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showToast(requireContext(), result.error)
                    Log.d("DetailFragment", result.error)
                }
            }
        }

        return binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}