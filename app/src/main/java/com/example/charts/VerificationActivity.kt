package com.example.charts

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verification)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra("email").toString()

        val verificationInput: AppCompatEditText = findViewById(R.id.verification_input)
        val submit: AppCompatButton = findViewById(R.id.submit)

        submit.setOnClickListener {
            val verification = verificationInput.text.toString()


            lifecycleScope.launch {
                val res = verify_user(email, verification)

                if(res != null) {
                    val intent = Intent(this@VerificationActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}