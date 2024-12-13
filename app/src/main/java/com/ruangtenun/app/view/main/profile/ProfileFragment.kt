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
import com.ruangtenun.app.data.local.pref.UserModel
import com.ruangtenun.app.data.repository.AuthRepository
import com.ruangtenun.app.databinding.FragmentProfileBinding
import com.ruangtenun.app.utils.DialogUtils.showDialog
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.view.authentication.AuthActivity
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import kotlin.getValue

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val authViewModel: AuthViewModel by viewModels {
            ViewModelFactory.getInstance(requireActivity().application)
        }

        binding.apply {
            cardFavorite.setOnClickListener {
                findNavController().navigate(R.id.navigation_favorite)
            }

            cardProduct.setOnClickListener {
                findNavController().navigate(R.id.navigation_partner)
            }

            cardLogout.setOnClickListener {
                showDialog(
                    requireContext(),
                    getString(R.string.title_logout),
                    getString(R.string.are_you_sure_want_to_logout),
                    getString(R.string.yes),
                    getString(R.string.cancel),
                    onPositiveClick = {
                        actionLogout(authViewModel)
                    }
                )
            }
        }

        authViewModel.getSession().observe(viewLifecycleOwner) { user ->
            displayUserData(user)
        }

        return binding.root
    }

    private fun displayUserData(user: UserModel?) {
        if (user != null) {
            binding.tvName.text = user.name
            binding.tvEmail.text = user.email
        }
    }

    private fun actionLogout(authViewModel: AuthViewModel) {
        authViewModel.logout()
        AuthRepository.clearInstance()
        ViewModelFactory.clearInstance()
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}