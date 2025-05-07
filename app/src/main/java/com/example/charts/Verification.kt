package com.example.charts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.handleAuthStateChanges
import io.github.jan.supabase.gotrue.providers.SessionState
import io.github.jan.supabase.supabase
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class Verification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set your layout here, perhaps showing a loading indicator
        // setContentView(R.layout.activity_initial_loading)

        observeAuthState()
    }

    private fun observeAuthState() {
        lifecycleScope.launch {
            supabase.auth.sessionManager.authStateChange()
                .filterNotNull() // Ensure we only process non-null states
                .collect { state ->
                    when (state) {
                        is SessionState.Loading -> {
                            // Session is still loading from storage
                            Log.d("AuthState", "SessionState: Loading")
                            // You might want to show a loading spinner here
                        }
                        is SessionState.SignedOut -> {
                            // User is signed out after loading
                            Log.d("AuthState", "SessionState: SignedOut")
                            navigateToRegister()
                        }
                        is SessionState.SignedIn -> {
                            // User is signed in after loading
                            Log.d("AuthState", "SessionState: SignedIn")
                            navigateToMain()
                        }
                        is SessionState.NetworkError -> {
                            // Handle network errors during session loading
                            Log.e("AuthState", "SessionState: NetworkError", state.cause)
                            // Decide how to handle network errors - maybe show an error message and navigate to register?
                            navigateToRegister() // Example: Navigate to register on network error
                        }
                        is SessionState.NotStarted -> {
                            // Initial state before loading begins
                            Log.d("AuthState", "SessionState: NotStarted")
                            // Still waiting for the loading process to begin
                        }
                    }
                }
        }
    }


}