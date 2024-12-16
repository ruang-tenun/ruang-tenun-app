package com.ruangtenun.app.view.main.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruangtenun.app.data.remote.response.FavoriteItem
import com.ruangtenun.app.databinding.FragmentFavoriteBinding
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import com.ruangtenun.app.viewmodel.favorite.AdapterFavorite
import com.ruangtenun.app.viewmodel.favorite.FavoriteViewModel
import kotlin.getValue

class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapterFavorite: AdapterFavorite

    private val favoriteViewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user != null) {
                favoriteViewModel.getFavoriteByUserId(user.token, user.id)
            }
        }

        setupRecyclerView()
        observeFavorite()

        return view
    }

    private fun setupRecyclerView() {
        binding.apply {
            val verticalLayout = LinearLayoutManager(requireContext())
            rvFavorite.layoutManager = verticalLayout
            adapterFavorite = AdapterFavorite()
            rvFavorite.adapter = adapterFavorite
        }
    }

    private fun observeFavorite() {
        favoriteViewModel.favoriteByUserId.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                ResultState.Idle -> showLoading(true)
                ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    val listFavorite = result.data
                    if (listFavorite.isEmpty()) {
                        showLoading(false)
                    } else {
                        setFavorite(listFavorite)
                    }
                    Log.d("FavoriteFragment", "Favorite: ${result.data}")
                }
            }
        }
    }

    private fun setFavorite(listFavorite: List<FavoriteItem>) {
        adapterFavorite.submitList(listFavorite)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}