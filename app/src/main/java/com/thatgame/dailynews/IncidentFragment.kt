package com.thatgame.dailynews

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID


class IncidentFragment: Fragment(), DatePickerFragment.Callbacks {

    private lateinit var incident: Incident
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var okayCheckBox: CheckBox
    private lateinit var sendReportButton: Button

    private val incidentDetailViewModel: IncidentDetailViewModel by lazy {
        ViewModelProvider(this).get(IncidentDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incident = Incident()
        val incidentId: UUID = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("INCIDENT_ID", UUID::class.java) as UUID
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        incidentDetailViewModel.loadIncident(incidentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.incident_fragment, container, false)

        titleField = view.findViewById(R.id.incident_title) as EditText
        dateButton = view.findViewById(R.id.dateButton) as Button
        okayCheckBox = view.findViewById(R.id.okayCheckBox) as CheckBox
        sendReportButton = view.findViewById(R.id.sendReportButton) as Button

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incidentDetailViewModel.incidentLiveData.observe(viewLifecycleOwner
        ) { incident ->
            incident?.let {
                this.incident = incident
                updateUI()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                incident.title = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }

        titleField.addTextChangedListener(titleWatcher)

        okayCheckBox.apply {
            setOnCheckedChangeListener{ _, isChecked ->
                incident.isOkay = isChecked
            }
        }

        dateButton.setOnClickListener{
            DatePickerFragment.newInstance(incident.date).apply {
                this@IncidentFragment.parentFragmentManager.setFragmentResultListener("Date", this) { key, bundle ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val result = bundle.getSerializable("Date", Date::class.java)
                        result?.let { it1 -> onDateSelected(it1) }
                    } else {
                        TODO("VERSION.SDK_INT < TIRAMISU")
                    }
                }
                show(this@IncidentFragment.parentFragmentManager, "Dialog_date")
            }
        }

        sendReportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getIncidentReport())
            }.also { intent -> startActivity(intent)}
        }
    }


    override fun onStop() {
        super.onStop()
        incidentDetailViewModel.saveIncident(incident)
    }

    private fun updateUI() {
        titleField.setText(incident.title)
        dateButton.setText(SimpleDateFormat("dd/MM/yyyy").format(incident.date))
        okayCheckBox.apply {
            isChecked = incident.isOkay
            jumpDrawablesToCurrentState()
        }
    }

    companion object {

        fun newInstance(incidentId: UUID): IncidentFragment {
            val args = Bundle().apply { putSerializable("INCIDENT_ID", incidentId) }
            return  IncidentFragment().apply { arguments = args }
        }
    }

    override fun onDateSelected(date: Date) {
        incident.date = date
        updateUI()
    }

    private fun getIncidentReport(): String {
        val okString = if (incident.isOkay) {
            getString(R.string.okay)
        } else {
            getString(R.string.not_okay)
        }

        val dateString = SimpleDateFormat("dd/MM/yyyy").format(incident.date)

        return getString(R.string.incident_report, incident.title, dateString, okString)
    }
}