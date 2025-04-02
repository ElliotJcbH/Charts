package com.example.charts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signInButton = findViewById<AppCompatButton>(R.id.signInButton)
        val createAccountButton = findViewById<AppCompatButton>(R.id.createAccountButton)

        signInButton.setOnClickListener {
            var intent = Intent(this, SignIn::class.java)

            startActivity(intent)
        }

        createAccountButton.setOnClickListener {
            var intent = Intent(this, CreateAccount::class.java)

            startActivity(intent)
        }

    }

}