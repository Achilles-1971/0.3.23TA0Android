package com.example.proect23.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.proect23.R
import com.example.proect23.databinding.ActivityLoginBinding
import com.example.proect23.ui.main.MainActivity
import com.example.proect23.util.PrefsManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val vm: AuthViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверяем наличие токена
        if (!PrefsManager.token.isNullOrBlank()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Инициализация UI
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Анимация появления элементов
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.cardUsername.startAnimation(fadeIn)
        binding.cardPassword.startAnimation(fadeIn)
        binding.btnLogin.startAnimation(fadeIn)
        binding.tvRegisterLink.startAnimation(fadeIn)

        // Подписываемся на состояние
        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnLogin.isEnabled = false
                    }
                    is AuthState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                    }
                }
            }
        }

        // Обработка клика на кнопку логина
        binding.btnLogin.setOnClickListener {
            val user = binding.etUsername.text.toString().trim()
            val pass = binding.etPassword.text.toString()

            // Валидация ввода
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.login(user, pass)
        }

        // Переход на экран регистрации
        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}