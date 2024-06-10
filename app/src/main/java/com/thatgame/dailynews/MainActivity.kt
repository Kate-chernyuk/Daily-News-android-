package com.thatgame.dailynews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID

class MainActivity : AppCompatActivity(), IncidentsListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident);

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = IncidentsListFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }

    }

    override fun onIncidentItemSelected(incidentID: UUID) {
        val fragment = IncidentFragment.newInstance(incidentID)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit()
    }

}
