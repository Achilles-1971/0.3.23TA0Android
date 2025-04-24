package com.example.proect23.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.proect23.R
import com.example.proect23.databinding.ActivityMainBinding
import com.example.proect23.ui.auth.LoginActivity
import com.example.proect23.util.PrefsManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* ▸ 1. Если токена нет – отправляем на логин и прекращаем работу */
        if (PrefsManager.token.isNullOrBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        /* ▸ 2. Нормальная инициализация главного экрана */
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
    }
}
