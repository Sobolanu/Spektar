package com.example.spektar.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    val client = createSupabaseClient( // add sign-in and sign-up via google
        supabaseUrl = "https://rlyotyktmhyflfyljpmr.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJseW90eWt0bWh5ZmxmeWxqcG1yIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjY0MTM3MDgsImV4cCI6MjA4MTk4OTcwOH0.eTwSGVag_npnTkiQtOCqBLxSidqMAM2psJEAN7h4TEQ"
    ) {
        install(Postgrest)
        install(Auth)
    }
}