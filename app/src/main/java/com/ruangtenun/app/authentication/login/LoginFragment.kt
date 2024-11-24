package com.ruangtenun.app.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ruangtenun.app.R
import com.ruangtenun.app.authentication.register.RegisterFragment

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.findViewById<TextView>(R.id.tv_sign_up_click).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    RegisterFragment(),
                    RegisterFragment::class.java.simpleName
                )
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}