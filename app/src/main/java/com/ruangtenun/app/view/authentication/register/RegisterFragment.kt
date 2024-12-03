package com.ruangtenun.app.view.authentication.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ruangtenun.app.R
import com.ruangtenun.app.data.ResultState
import com.ruangtenun.app.view.authentication.login.LoginFragment
import com.ruangtenun.app.databinding.FragmentRegisterBinding
import com.ruangtenun.app.view.authentication.AuthViewModel
import com.ruangtenun.app.view.authentication.AuthViewModelFactory

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
            AuthViewModelFactory.getInstance(requireContext())
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
                is ResultState.Success -> {
                    showLoading(false)
                    showDialog(
                        requireContext(),
                        "Register berhasil",
                        result.data.message.toString(),
                        "Lanjut",
                        true
                    )
                }

                is ResultState.Error -> {
                    showLoading(false)
                    showDialog(
                        requireContext(),
                        "Register gagal",
                        "Registrasi gagal: ${result.error}",
                        "Coba Lagi",
                        false
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

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
        setOnClick: String,
        moveToLogin: Boolean = false
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(setOnClick) { dialog, which ->
                if (moveToLogin) {
                    moveToLogin()
                } else {
                    dialog.dismiss()
                }
            }
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}