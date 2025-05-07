package com.example.charts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

class SessionManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeAuthState()
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            supabase.auth.sessionStatus
                .collect { status ->
                    when (status) {
                        SessionStatus.Initializing -> {
                            Log.d("AuthState", "SessionStatus: Initializing")
                            // Handle initialization state
                        }
                        is SessionStatus.RefreshFailure -> {
                            Log.d("AuthState", "SessionStatus: RefreshFailure")
                            // Handle refresh failure - perhaps show an error message
                            navigateToRegister()
                        }
                        is SessionStatus.Authenticated -> {
                            // User is signed in
                            Log.d("AuthState", "SessionStatus: Authenticated")
                            navigateToMain()
                        }
                        is SessionStatus.NotAuthenticated -> {
                            // User is signed out
                            Log.d("AuthState", "SessionStatus: NotAuthenticated")
                            navigateToRegister()
                        }
                    }
                }
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, Register::class.java) // Replace Register::class.java with your Register Activity
        startActivity(intent)
        finish() // Finish the current activity so the user can't go back
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java) // Replace MainActivity::class.java with your Main Activity
        startActivity(intent)
        finish() // Finish the current activity so the user can't go back
    }
}