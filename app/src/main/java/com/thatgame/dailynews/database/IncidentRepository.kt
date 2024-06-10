package com.thatgame.dailynews.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.thatgame.dailynews.Incident
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "incidents-database"

class IncidentRepository private constructor(context: Context){

    private val database: IncidentDatabase = Room.databaseBuilder(context.applicationContext, IncidentDatabase::class.java, DATABASE_NAME).build()

    private val incidentDao = database.IncidentDao()

    private val executor = Executors.newSingleThreadExecutor()

    private val filesDir = context.applicationContext.filesDir

    fun getIncidents(): LiveData<List<Incident>> = incidentDao.getIncidents()

    fun getIncident(id: UUID): LiveData<Incident?> = incidentDao.getIncident(id)

    fun updateIncident(incident: Incident) {
        executor.execute{
            incidentDao.updateIncident(incident)
        }
    }

    fun addIncident(incident: Incident) {
        executor.execute{
            incidentDao.addIncident(incident)
        }
    }


    companion object {
        private var INSTANCE: IncidentRepository? = null

        fun initialize(context: Context)  {
            if (INSTANCE == null) {
                INSTANCE = IncidentRepository(context)
            }
        }

        fun get(): IncidentRepository {
            return INSTANCE ?:
            throw IllegalStateException("IncidentRepository must be initialized")
        }
    }
}