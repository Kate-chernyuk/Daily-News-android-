package com.thatgame.dailynews

import android.app.Application
import com.thatgame.dailynews.database.IncidentRepository

class IncidentIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        IncidentRepository.initialize(this)
    }
}