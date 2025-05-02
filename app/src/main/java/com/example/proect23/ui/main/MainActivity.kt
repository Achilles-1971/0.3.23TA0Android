package com.example.proect23.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.proect23.R
import com.example.proect23.ui.auth.LoginActivity
import com.example.proect23.util.PrefsManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (PrefsManager.token.isNullOrBlank() || PrefsManager.refreshToken.isNullOrBlank()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_enterprises -> {
                    if (navController.currentDestination?.id != R.id.enterprisesFragment) {
                        navController.navigate(R.id.action_to_enterprises)
                    }
                    true
                }
                R.id.nav_indicators -> {
                    if (navController.currentDestination?.id != R.id.indicatorsFragment) {
                        navController.navigate(R.id.action_to_indicators)
                    }
                    true
                }
                R.id.nav_currencies -> {
                    if (navController.currentDestination?.id != R.id.currenciesFragment) {
                        navController.navigate(R.id.action_to_currencies)
                    }
                    true
                }
                R.id.profileFragment -> {
                    if (navController.currentDestination?.id != R.id.profileFragment) {
                        navController.navigate(R.id.action_to_profile)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
