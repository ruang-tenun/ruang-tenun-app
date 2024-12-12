package com.ruangtenun.app.view.main.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentDetailBinding
import com.ruangtenun.app.databinding.FragmentDetailCatalogBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ToastUtils.showToast
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.main.MainViewModel

class DetailCatalogFragment : Fragment() {

    private var _binding: FragmentDetailCatalogBinding? = null
    private val binding get() = _binding!!

    private var productId: Int? = null

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var token: String =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NSwidXNlcm5hbWUiOiJ0ZXN0IiwiZW1haWwiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTczMzk4Nzg5NywiZXhwIjoxNzMzOTkxNDk3fQ.90Gg-oBh7e7K4WQMXrD5c8JQ4kQLspbmmuQ_WlgCrqs"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailCatalogBinding.inflate(layoutInflater, container, false)

        productId = arguments?.getInt("productId")

        mainViewModel.fetchProductDetail(token, productId!!)

        mainViewModel.productDetailState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    val product = result.data
                    binding.apply {
                        tvProductName.text = product.name
                        tvOfflineLocation.text = product.latitude.toString()
                        button.setOnClickListener {
                            val link = product.ecommerceUrl
                            if (!link.isNullOrEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                startActivity(intent)
                            } else {
                                showToast(requireContext(), getString(R.string.link_not_found))
                            }

                        }
                        Glide.with(requireContext())
                            .load(product.imageUrl)
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