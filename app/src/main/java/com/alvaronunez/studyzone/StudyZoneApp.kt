package com.alvaronunez.studyzone

import android.app.Application
import com.alvaronunez.studyzone.presentation.di.AppComponent
import com.alvaronunez.studyzone.presentation.di.DaggerAppComponent

class StudyZoneApp: Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.factory().create(this)
    }

}