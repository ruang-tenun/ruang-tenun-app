package com.ruangtenun.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ruangtenun.app.databinding.ActivitySplashScreenBinding
import com.ruangtenun.app.utils.ViewModelFactory
import com.ruangtenun.app.view.authentication.AuthActivity
import com.ruangtenun.app.view.main.MainActivity
import com.ruangtenun.app.view.onboarding.OnboardingActivity
import com.ruangtenun.app.viewmodel.authentication.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this.application)
        )[AuthViewModel::class.java]

        lifecycleScope.launch {
            delay(3000)
            checkUserStatus()
        }

    }

    private fun checkUserStatus() {
        val sharedPreferences = getSharedPreferences("appPreferences", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

        viewModel.getSession().observe(this) { user ->
            when {
                isFirstLaunch -> {
                    sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
                    navigateToOnboardingActivity()
                }

                user?.isLogin == true -> navigateToMainActivity()

                else -> navigateToAuthActivity()
            }
        }
    }


    private fun navigateToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToOnboardingActivity() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}