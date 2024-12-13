package com.ruangtenun.app.view.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ruangtenun.app.R
import com.ruangtenun.app.view.authentication.login.LoginFragment
import com.ruangtenun.app.databinding.FragmentRegisterBinding
import com.ruangtenun.app.utils.DialogUtils.showDialog
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        val authViewModel: AuthViewModel by viewModels {
            ViewModelFactory.getInstance(requireActivity().application)
        }

        binding.apply {
            backButton.setOnClickListener {
                moveToLogin()
            }
            tvSignInClick.setOnClickListener {
                moveToLogin()
            }
            registerButton.setOnClickListener {
                registerAction(authViewModel)
            }
        }

        observeView(authViewModel)

        return view
    }

    private fun observeView(authViewModel: AuthViewModel) {
        authViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }
                is ResultState.Success -> {
                    showLoading(false)
                    showDialog(
                        requireContext(),
                        getString(R.string.registration_successful),
                        result.data.message.toString(),
                        getString(R.string.next),
                        onPositiveClick = { moveToLogin() }
                    )
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showDialog(
                        requireContext(),
                        getString(R.string.register_failed),
                        getString(R.string.registration_failed_with_error, result.error),
                        getString(R.string.try_again)
                    )
                }

                is ResultState.Loading -> {
                    showLoading(true)
                }

            }
        }
    }

    private fun registerAction(authViewModel: AuthViewModel) {
        val name = binding.edInputFullName.text.toString()
        val email = binding.edInputEmail.text.toString()
        val password = binding.edInputSetPassword.text.toString()

        authViewModel.register(name, email, password)
    }

    private fun moveToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                LoginFragment(),
                LoginFragment::class.java.simpleName
            )
            .commit()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}