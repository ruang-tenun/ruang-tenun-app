package com.ruangtenun.app.authentication.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ruangtenun.app.R
import com.ruangtenun.app.authentication.login.LoginFragment

class RegisterFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            moveToLogin()
        }

        view.findViewById<TextView>(R.id.tv_sign_in_click).setOnClickListener {
            moveToLogin()
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