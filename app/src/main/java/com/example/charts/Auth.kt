package com.example.charts

import android.util.Log
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.coroutineScope


suspend fun create_user(myEmail: String, myPassword: String): Result<UserInfo?> {
    try {
        val userSession = supabase.auth.signUpWith(Email) {
            email = myEmail
            password = myPassword
        }
        return Result.success(userSession)
    } catch (e: Exception) {
        print("Error fetching top 10 weekly: ${e.message}")
        throw (e)
    }
}

suspend fun signin_user(myEmail: String, myPassword: String): Result<Unit> {
    try {
        val res = supabase.auth.signInWith(Email) {
            email = myEmail
            password = myPassword
        }

        return Result.success(res)
    } catch (e: Exception) {
        print("Error fetching top 10 weekly: ${e.message}")
        throw (e)
    }
}

suspend fun verify_user(myEmail: String, myToken: String): Result<Unit> {
    try {
        val res = supabase.auth.verifyEmailOtp(type = OtpType.Email.EMAIL, email = "example@email.com", token = "token")

        return Result.success(res)

    } catch (e: Exception) {
        print("Verification Error: ${e.message}")
        throw(e)
    }
}

//suspend fun create_user_record(username: String): Result<Unit> {
//    try {
//        val res = User
//    }
//}
