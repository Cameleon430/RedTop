package com.example.redtop.presenter.main

import android.app.Application
import com.example.redtop.di.ServiceLocator

class RedTopApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.initializeDatabase(this)
    }
}