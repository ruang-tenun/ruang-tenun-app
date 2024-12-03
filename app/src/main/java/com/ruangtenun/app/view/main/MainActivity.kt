package com.ruangtenun.app.view.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ruangtenun.app.R
import com.ruangtenun.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            if (binding.navView.visibility == View.VISIBLE) {
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            } else {
                v.setPadding(systemBars.left, systemBars.top, systemBars.right,  systemBars.bottom)
            }
            insets
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_search, R.id.navigation_partner, R.id.navigation_history, R.id.navigation_upload_product, R.id.navigation_profile -> {
                    binding.navView.visibility = View.GONE
                    adjustWindowInsetsForNavView(false)
                }
                else -> {
                    binding.navView.visibility = View.VISIBLE
                    adjustWindowInsetsForNavView(true)
                }
            }
        }

        navView.setupWithNavController(navController)
    }

    private fun adjustWindowInsetsForNavView(isNavViewVisible: Boolean) {
        binding.root.requestApplyInsets()
    }
}
