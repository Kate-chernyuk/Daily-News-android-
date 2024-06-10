package com.thatgame.dailynews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.thatgame.dailynews.database.IncidentRepository
import java.util.UUID

class IncidentDetailViewModel() : ViewModel() {

    private val incidentRepository = IncidentRepository.get()
    private val incidentIdLiveData = MutableLiveData<UUID>()

    var incidentLiveData: LiveData<Incident?> = incidentIdLiveData.switchMap {incidentId -> incidentRepository.getIncident(incidentId)}

    fun loadIncident(incidentId: UUID) {
        incidentIdLiveData.value = incidentId
    }

    fun saveIncident(incident: Incident) {
        incidentRepository.updateIncident(incident)
    }
}