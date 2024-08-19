package com.example.tennis.ui.calendar

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tennis.R

class CalendarFragment : Fragment() {

    companion object {
        fun newInstance() = CalendarFragment()
    }

    private lateinit var viewModel: CalendarViewModel
    private lateinit var addButton: Button
    private lateinit var reservationRecyclerView: RecyclerView
    private lateinit var reservationAdapter: ReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        addButton = view.findViewById(R.id.addButton)
        reservationRecyclerView = view.findViewById(R.id.reservationRecyclerView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)

        setupRecyclerView()

        addButton.setOnClickListener {
            showAddReservationDialog()
        }

        viewModel.reservations.observe(viewLifecycleOwner) { reservations ->
            reservationAdapter.submitList(reservations)
        }
    }

    private fun setupRecyclerView() {
        reservationAdapter = ReservationAdapter()
        reservationRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reservationAdapter
        }
    }

    private fun showAddReservationDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_add_reservation)

        val editTextDate = dialog.findViewById<EditText>(R.id.editTextDate)
        val editTextTime = dialog.findViewById<EditText>(R.id.editTime)
        val editTextDetails = dialog.findViewById<EditText>(R.id.editTextDetails)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val date = editTextDate.text.toString()
            val time = editTextTime.text.toString()
            val details = editTextDetails.text.toString()

            if (date.isNotEmpty() && time.isNotEmpty()) {
                // TODO: ViewModel을 통해 예약 정보 저장
                viewModel.addReservation(date, time, details)
                Toast.makeText(context, "예약이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(context, "날짜와 시간을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}

class ReservationAdapter : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {
    private var reservations: List<CalendarViewModel.Reservation> = emptyList()

    fun submitList(newReservations: List<CalendarViewModel.Reservation>) {
        reservations = newReservations
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservations[position])
    }

    override fun getItemCount() = reservations.size

    class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        private val timeTextView: TextView = itemView.findViewById(R.id.textViewTime)
        private val detailsTextView: TextView = itemView.findViewById(R.id.textViewDetails)

        fun bind(reservation: CalendarViewModel.Reservation) {
            dateTextView.text = reservation.date
            timeTextView.text = reservation.time
            detailsTextView.text = reservation.details
        }
    }
}