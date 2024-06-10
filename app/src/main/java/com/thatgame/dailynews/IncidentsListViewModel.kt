package com.thatgame.dailynews

import androidx.lifecycle.ViewModel
import com.thatgame.dailynews.database.IncidentRepository

class IncidentsListViewModel: ViewModel() {
    private val incidentRepository = IncidentRepository.get()
    val incidentsListLiveData = incidentRepository.getIncidents()
    fun addIncident(incident: Incident) = incidentRepository.addIncident(incident)
}