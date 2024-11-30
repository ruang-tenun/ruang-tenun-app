package com.ruangtenun.app.view.main.partner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.FragmentPartnerBinding

class PartnerFragment : Fragment() {

    private lateinit var binding: FragmentPartnerBinding
//    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartnerBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUploadProduct.setOnClickListener {
            findNavController().navigate(R.id.navigation_upload_product)
        }

//        adapter = ProductAdapter()
//        binding.rvPartnerProduct.adapter = adapter
//
//        binding.swipeRefreshLayout.setOnRefreshListener {
//            loadData()
//        }
//
//        loadData()

        return binding.root
    }

//    private fun loadData() {
//        val products = listOf<Product>()
//
//        binding.apply {
//            if (products.isEmpty()) {
//                rvPartnerProduct.visibility = View.GONE
//                emptyTextView.visibility = View.VISIBLE
//            } else {
//                rvPartnerProduct.visibility = View.VISIBLE
//                emptyTextView.visibility = View.GONE
//                adapter.submitList(products)
//            }
//            swipeRefreshLayout.isRefreshing = false
//        }
//    }

}