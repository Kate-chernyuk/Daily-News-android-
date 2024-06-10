package com.thatgame.dailynews.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.thatgame.dailynews.Incident
import java.util.UUID

@Dao
interface IncidentDAO {
    @Query("SELECT * FROM Incident")
    fun getIncidents(): LiveData<List<Incident>>

    @Query("SELECT * FROM Incident WHERE id=(:id)")
    fun getIncident(id: UUID): LiveData<Incident?>

    @Update
    fun updateIncident(incident: Incident)

    @Insert
    fun addIncident(incident: Incident)
}