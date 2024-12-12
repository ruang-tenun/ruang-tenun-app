package com.ruangtenun.app.view.authentication.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ruangtenun.app.R
import com.ruangtenun.app.data.repository.AuthRepository
import com.ruangtenun.app.databinding.FragmentLoginBinding
import com.ruangtenun.app.utils.DialogUtils.showDialog
import com.ruangtenun.app.utils.ResultState
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.view.authentication.register.RegisterFragment
import com.ruangtenun.app.view.main.MainActivity
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel

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
            ViewModelFactory.getInstance(requireActivity().application)
        }

        binding.apply {
            tvSignUpClick.setOnClickListener {
                moveToRegister()
            }

            loginButton.setOnClickListener {
//                loginAction(authViewModel)
                moveToMain()
            }

            googleLogin.setOnClickListener {
                showDialog(
                    requireContext(),
                    getString(R.string.login_failed),
                    getString(R.string.feature_not_yet_available), getString(R.string.ok)
                )
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
                getString(R.string.login_failed),
                getString(R.string.email_and_password_cannot_be_empty),
                getString(R.string.try_again)
            )
        }
    }

    private fun observeView(authViewModel: AuthViewModel) {
        authViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Idle -> {
                }
                is ResultState.Success -> {
                    AuthRepository.clearInstance()
                    ViewModelFactory.clearInstance()
                    showLoading(false)
                    moveToMain()
                }

                is ResultState.Error -> {
                    showLoading(false)
                    val errorMessage = if (result.error.contains("email or password is wrong")) {
                        "Email atau Password salah. Silakan coba lagi."
                    } else if (result.error.contains("email is not found")) {
                        "Akun tidak ditemukan. Silakan daftar terlebih dahulu."
                    } else {
                        getString(R.string.login_failed_with_error, result.error)
                    }
                    showDialog(
                        requireContext(),
                        getString(R.string.login_failed),
                        errorMessage,
                        getString(R.string.try_again)
                    )
                }

                is ResultState.Loading -> {
                    showLoading(true)
                }

            }
        }
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