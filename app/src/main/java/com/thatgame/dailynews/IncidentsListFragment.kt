package com.thatgame.dailynews

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.UUID

class IncidentsListFragment: Fragment() {

    interface Callbacks {
        fun onIncidentItemSelected(incidentID: UUID)
    }

    private var callbacks: Callbacks? = null
    private lateinit var incidentsRecyclerView: RecyclerView
    private var adapter: IncidentsAdapter? = IncidentsAdapter(emptyList())

    private val incidentsListViewModel: IncidentsListViewModel by lazy { ViewModelProvider(this).get(IncidentsListViewModel::class.java) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_incident_list, container, false)

        incidentsRecyclerView = view.findViewById(R.id.incident_recycler_view) as RecyclerView
        incidentsRecyclerView.layoutManager = LinearLayoutManager(context)
        incidentsRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incidentsListViewModel.incidentsListLiveData.observe(viewLifecycleOwner, Observer { incidents -> incidents?.let {updateUI(incidents)} })

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.incident_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.new_incident -> {
                        val incident = Incident()
                        incidentsListViewModel.addIncident(incident)
                        callbacks?.onIncidentItemSelected(incident.id)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(incidents: List<Incident>) {
        adapter = IncidentsAdapter(incidents)
        incidentsRecyclerView.adapter = adapter
    }

    private inner class IncidentsHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var incident: Incident

        val titleTextView: TextView = itemView.findViewById(R.id.incident_title)
        val dateTextView: TextView = itemView.findViewById(R.id.incident_date)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(incident: Incident) {
            this.incident = incident
            titleTextView.text = this.incident.title
            dateTextView.text = SimpleDateFormat("dd/MM/yyyy").format(this.incident.date)
        }

        override fun onClick(v: View?) {
            callbacks?.onIncidentItemSelected(incident.id)
        }
    }

    private inner class IncidentsAdapter(var incidents: List<Incident>): RecyclerView.Adapter<IncidentsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentsHolder {
            val view = layoutInflater.inflate(R.layout.list_item_incident, parent, false)
            return IncidentsHolder(view)
        }

        override fun getItemCount(): Int = incidents.size

        override fun onBindViewHolder(holder: IncidentsHolder, position: Int) {
            val incident = incidents[position]
            holder.bind(incident)
        }

    }

}