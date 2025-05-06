package com.example.charts

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://lzizqllwfmwclgutkwkv.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx6aXpxbGx3Zm13Y2xndXRrd2t2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDY0MjcyMDksImV4cCI6MjA2MjAwMzIwOX0.kTQumqEUq7a8M7pk6P0_5YjcRZ-NxRsOPdbam6koDN8"
) {
    install(Auth)
    install(Postgrest)
    //install other modules
}
