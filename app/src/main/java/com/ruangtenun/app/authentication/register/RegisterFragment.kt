package com.ruangtenun.app.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ruangtenun.app.R
import com.ruangtenun.app.authentication.login.LoginFragment
import com.ruangtenun.app.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.apply {
            backButton.setOnClickListener {
                moveToLogin()
            }
            tvSignInClick.setOnClickListener {
                moveToLogin()
            }
        }

        return view
    }

    private fun moveToLogin() {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                LoginFragment(),
                LoginFragment::class.java.simpleName)
            .commit()
    }
}