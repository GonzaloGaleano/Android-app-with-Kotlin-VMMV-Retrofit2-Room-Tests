package com.example.jsonplaceholderposts

import android.app.Application
import android.content.Context

class PostApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object{
        private lateinit var appContext: Context
        fun appContext(): Context {
            return appContext
        }
    }
}