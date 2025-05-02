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
import com.example.proect23.databinding.ActivityRegisterBinding
import com.example.proect23.ui.main.MainActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val vm: AuthViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.cardUsername.startAnimation(fadeIn)
        binding.cardPassword.startAnimation(fadeIn)
        binding.btnRegister.startAnimation(fadeIn)
        binding.tvLoginLink.startAnimation(fadeIn)

        lifecycleScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is AuthState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AuthState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@RegisterActivity, state.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            val user = binding.etUsername.text.toString().trim()
            val pass = binding.etPassword.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 8) {
                Toast.makeText(this, "Пароль должен содержать минимум 8 символов", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.register(user, pass)
        }

        binding.tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}