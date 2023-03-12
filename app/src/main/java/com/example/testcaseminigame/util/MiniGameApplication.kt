package com.example.testcaseminigame.util

import android.app.Application
import android.content.Context

class MiniGameApplication : Application() {
    init {
        appInstance = this
    }

    companion object {
        private var appInstance: MiniGameApplication? = null

        fun applicationContext(): Context {
            return appInstance!!.applicationContext
        }
    }
}