package com.alvaronunez.studyzone

import android.app.Application
import com.alvaronunez.studyzone.presentation.initDI

class StudyZoneApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }

}