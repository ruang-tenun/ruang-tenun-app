package com.ruangtenun.app.view.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ruangtenun.app.R
import com.ruangtenun.app.data.UserRepository
import com.ruangtenun.app.databinding.FragmentProfileBinding
import com.ruangtenun.app.view.authentication.AuthActivity
import com.ruangtenun.app.view.authentication.AuthViewModel
import com.ruangtenun.app.view.authentication.AuthViewModelFactory
import kotlin.getValue

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val authViewModel: AuthViewModel by viewModels {
            AuthViewModelFactory.Companion.getInstance(requireContext())
        }

        binding.apply {
            cardProduct.setOnClickListener {
                findNavController().navigate(R.id.navigation_partner)
            }

            cardLogout.setOnClickListener {
                authViewModel.logout()
                UserRepository.clearInstance()
                AuthViewModelFactory.clearInstance()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        return binding.root
    }

}