package com.example.charts

import android.content.Intent
import android.os.Bundle
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

class SignIn : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sign_in_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        val submitButton = findViewById<AppCompatButton>(R.id.submit)
        val progressBar: ProgressBar = findViewById(R.id.buttonProgressBar)

        submitButton.setOnClickListener {
            submitButton.isEnabled = false
            submitButton.setText("")
            progressBar.visibility = View.VISIBLE

            val email = findViewById<AppCompatEditText>(R.id.email).text.toString()
            val password = findViewById<AppCompatEditText>(R.id.password).text.toString()

            lifecycleScope.launch {
                val res = signin_user(email, password)

                if(res != null) {
                    submitButton.isEnabled = true
                    submitButton.setText("Continue")
                    progressBar.visibility = View.GONE

                    var intent = Intent(this@SignIn, MainActivity::class.java)
                    intent.putExtra("email", email)

                    startActivity(intent)
                }
            }


        }

    }

}