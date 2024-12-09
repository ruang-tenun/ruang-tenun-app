package com.ruangtenun.app.view.main.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruangtenun.app.databinding.FragmentProductBinding
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.product.ProductViewModel

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(layoutInflater, container, false)

//        setupRecyclerView()
//        setupAdapter()
//        observeViewModel()

        return _binding!!.root
    }

//    private fun setupRecyclerView() {
//        binding?.apply {
//            val verticalLayout = LinearLayoutManager(requireContext())
//            rvFavoriteEvent.layoutManager = verticalLayout
//            val itemFavoriteEventDecoration =
//                DividerItemDecoration(requireContext(), verticalLayout.orientation)
//            rvFavoriteEvent.addItemDecoration(itemFavoriteEventDecoration)
//        }
//    }
//
//    private fun setupAdapter() {
//        adapterFavoriteEvent = AdapterFavoriteEvent { eventId ->
//            val bundle = Bundle().apply {
//                eventId?.let { putInt("eventId", it) }
//            }
//            findNavController().navigate(R.id.navigation_detail, bundle)
//        }
//
//        binding?.rvFavoriteEvent?.adapter = adapterFavoriteEvent
//    }
//
//    private fun observeViewModel() {
//        binding?.apply {
//            mainViewModel.allFavoriteEvents.observe(viewLifecycleOwner) { listItems ->
//                Log.d("FavoriteFragment", "Observed Favorite Events: $listItems")
//                setFavoriteEvent(listItems)
//                mainViewModel.clearErrorMessage()
//            }
//
//            mainViewModel.isLoading.observe(viewLifecycleOwner) {
//                showLoading(it, progressBar)
//            }
//
//            mainViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
//                handleError(
//                    isError = errorMessage != null,
//                    message = errorMessage,
//                    errorTextView = tvErrorMessage,
//                    refreshButton = btnRefresh,
//                    recyclerView = rvFavoriteEvent
//                ) {
//                    mainViewModel.getAllFavoriteEvent()
//                }
//            }
//        }
//    }
//
//    private fun setFavoriteEvent(listFavoriteEvent: List<FavoriteEvent>) {
//        adapterFavoriteEvent.submitList(listFavoriteEvent)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}