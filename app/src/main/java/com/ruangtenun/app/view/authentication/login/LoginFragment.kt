package com.ruangtenun.app.view.authentication.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ruangtenun.app.R
import com.ruangtenun.app.data.ResultState
import com.ruangtenun.app.data.UserRepository
import com.ruangtenun.app.databinding.FragmentLoginBinding
import com.ruangtenun.app.view.authentication.AuthViewModel
import com.ruangtenun.app.view.authentication.AuthViewModelFactory
import com.ruangtenun.app.view.authentication.register.RegisterFragment
import com.ruangtenun.app.view.main.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        val authViewModel: AuthViewModel by viewModels {
            AuthViewModelFactory.Companion.getInstance(requireContext())
        }

        binding.apply {
            tvSignUpClick.setOnClickListener {
                moveToRegister()
            }

            loginButton.setOnClickListener {
                loginAction(authViewModel)
            }

            googleLogin.setOnClickListener {
                showDialog(requireContext(), "Login gagal", "Fitur belum tersedia", "Ok", false)
            }
        }

        observeView(authViewModel)

        return view
    }

    private fun loginAction(authViewModel: AuthViewModel) {
        val email = binding.edInputEmail.text.toString()
        val password = binding.edInputPassword.text.toString()

        if (email.isNotBlank() && password.isNotBlank()) {
            authViewModel.login(email, password)
        } else {
            showDialog(
                requireContext(),
                "Login gagal",
                "Email dan password tidak boleh kosong.",
                "Coba Lagi",
                false
            )
        }
    }

    private fun observeView(authViewModel: AuthViewModel) {
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Success -> {
                    UserRepository.clearInstance()
                    AuthViewModelFactory.Companion.clearInstance()
                    showLoading(false)
                    showDialog(
                        requireContext(),
                        "Login berhasil",
                        "Anda berhasil login",
                        "Lanjut",
                        true
                    )
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val errorMessage = if (result.error.contains("Invalid password")) {
                        "Password salah. Silakan coba lagi."
                    } else if (result.error.contains("email is not found")) {
                        "Akun tidak ditemukan. Silakan daftar terlebih dahulu."
                    } else {
                        "Login gagal: ${result.error}"
                    }
                    showDialog(requireContext(), "Login gagal", errorMessage, "Coba Lagi", false)
                }

                is ResultState.Loading -> {
                    showLoading(true)
                }

                null -> false
            }
        }
    }

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
        setOnClick: String,
        moveToMain: Boolean = false
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(setOnClick) { dialog, which ->
                dialog.dismiss()
                if (moveToMain) {
                    moveToMain()
                }
            }
            .show()
    }

    private fun moveToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun moveToRegister() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                RegisterFragment(),
                RegisterFragment::class.java.simpleName
            )
            .commit()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}