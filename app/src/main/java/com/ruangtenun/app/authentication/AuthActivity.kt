package com.ruangtenun.app.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ruangtenun.app.R
import com.ruangtenun.app.authentication.login.LoginFragment
import com.ruangtenun.app.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, loginFragment)
            .commit()
    }
}