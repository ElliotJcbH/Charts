package com.example.charts

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CreateAccount : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_create_account)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_account_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val submitButton = findViewById<AppCompatButton>(R.id.submit)
        val progressBar = findViewById<ProgressBar>(R.id.buttonProgressBar)
        val usernameInput = findViewById<AppCompatEditText>(R.id.username_input)
        val emailInput = findViewById<AppCompatEditText>(R.id.email_input)
        val passwordInput1 = findViewById<AppCompatEditText>(R.id.password_input_1)
        val passwordInput2 = findViewById<AppCompatEditText>(R.id.password_input_2)

        backButton.setOnClickListener {
            finish()
        }

        submitButton.setOnClickListener {
            submitButton.isEnabled = false
            submitButton.setText("")
            progressBar.visibility = View.VISIBLE

            val username = usernameInput.text.toString()
            val email = emailInput.text.toString()
            val password1 = passwordInput1.text.toString()
            val password2 = passwordInput2.text.toString()

            if(email.isBlank()) return@setOnClickListener

            if(password1.isBlank()) return@setOnClickListener

            if(password1 != password2) return@setOnClickListener

            lifecycleScope.launch() {
                val res = create_user(email, password1)

                if(res != null) {
                    progressBar.visibility = View.GONE
                    submitButton.setText("Continue")
                    submitButton.isEnabled = true
                    val intent = Intent(this@CreateAccount, VerificationActivity::class.java)
                    intent.putExtra("email", email)
                    startActivity(intent)
                }
            }

        }

    }

}