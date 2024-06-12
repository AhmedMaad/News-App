package com.maad.newsapp.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.maad.newsapp.R
import com.maad.newsapp.databinding.ActivityLoginBinding
import com.maad.newsapp.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        binding.notUserTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "Missing Field/s!", Toast.LENGTH_SHORT).show()
            else {
                binding.progress.isVisible = true
                login(email, password)
            }

        }

        binding.forgotPassTv.setOnClickListener {
            val email = binding.emailEt.text.toString()
            sendPasswordResetEmail(email)
        }

    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                    checkEmailVerification()
                else {
                    binding.progress.isVisible = false
                    Toast.makeText(this, "${task.exception?.localizedMessage}", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        if (email.isBlank())
            binding.emailEt.error = "Required!"
        else {
            binding.progress.isVisible = true
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                binding.progress.isVisible = false
                if (task.isSuccessful)
                    Toast.makeText(this, "Email sent!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkEmailVerification() {
        binding.progress.isVisible = false
        if (auth.currentUser!!.isEmailVerified) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else
            Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show()
    }

}