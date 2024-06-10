package com.thatgame.dailynews

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment: DialogFragment() {

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateListener = DatePickerDialog.OnDateSetListener{
            _: DatePicker, year: Int, month: Int, date: Int ->

            val resultDate : Date = GregorianCalendar(year, month, date).time

            val bundle = Bundle().apply {
                //putString("Date", resultDate.toString())
                putSerializable("Date", resultDate)
            }

            parentFragmentManager.setFragmentResult("Date", bundle)
            }


        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("Date", Date::class.java) as Date
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        return DatePickerDialog( requireContext(), dateListener, initialYear, initialMonth, initialDayOfWeek )
    }

    companion object {
        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable("Date", date)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}