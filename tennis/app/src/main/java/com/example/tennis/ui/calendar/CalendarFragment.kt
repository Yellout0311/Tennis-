package com.example.tennis.ui.calendar

import CalendarViewModel
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tennis.R
import com.example.tennis.databinding.FragmentCalendarBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var calendarOverlay: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        calendarOverlay = binding.root.findViewById(R.id.calendarOverlay)

        binding.btnAddReservation.setOnClickListener {
            openAddReservationDialog()
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            updateCalendar()
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            showReservationDetails(selectedDate)
        }

        return binding.root
    }

    private fun updateCalendar() {
        val calendarView = binding.calendarView
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Clear previous drawings
        calendarOverlay.background = null

        viewModel.getAllReservations().forEach { (dateString, reservation) ->
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date ?: return@forEach
            drawMarker(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                reservation.color
            )
        }
    }
    private fun drawMarker(year: Int, month: Int, day: Int, color: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val cellWidth = binding.calendarView.width / 7f
        val cellHeight = (binding.calendarView.height - binding.calendarView.paddingTop - binding.calendarView.paddingBottom) / 6f

        val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH) - 1
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

        val x = dayOfWeek * cellWidth + cellWidth / 2
        val y = weekOfMonth * cellHeight + cellHeight / 2 + binding.calendarView.paddingTop

        val bitmap = (calendarOverlay.background as? BitmapDrawable)?.bitmap
            ?: Bitmap.createBitmap(calendarOverlay.width, calendarOverlay.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            this.color = color
            style = Paint.Style.FILL
            alpha = 128 // 반투명 설정 (0-255)
        }

        canvas.drawCircle(x, y, cellWidth / 4, paint)

        calendarOverlay.background = BitmapDrawable(resources, bitmap)
    }

    private fun showReservationDetails(date: String) {
        val reservation = viewModel.getReservation(date)
        reservation?.let {
            val details = """
                예약 날짜: ${it.date}
                시간: ${it.hour}:${it.minute}
                코트 번호: ${it.courtNumber}
                옵션: ${it.option}
            """.trimIndent()

            AlertDialog.Builder(requireContext())
                .setTitle("예약 세부사항")
                .setMessage(details)
                .setPositiveButton("확인", null)
                .create()
                .show()
        } ?: run {
            Toast.makeText(requireContext(), "예약된 일정이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAddReservationDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_reservation, null)
        val calendarView = dialogView.findViewById<CalendarView>(R.id.dialogCalendarView)

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("예약 추가")
            .setNegativeButton("취소", null)
            .setPositiveButton("확인") { _, _ ->
                val selectedDate = Date(calendarView.date)
                openAddDetailsDialog(selectedDate)
            }
        builder.create().show()
    }

    private fun openAddDetailsDialog(selectedDate: Date) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_details, null)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)
        val courtNumberEditText = dialogView.findViewById<EditText>(R.id.etCourtNumber)
        val optionsRadioGroup = dialogView.findViewById<RadioGroup>(R.id.rgOptions)
        val confirmButton = dialogView.findViewById<Button>(R.id.btnConfirmDetails)

        // 24시간 형식을 설정
        timePicker.setIs24HourView(true)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("세부사항 입력")
            .setNegativeButton("취소", null)
            .create()

        confirmButton.setOnClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            val courtNumber = courtNumberEditText.text.toString().toIntOrNull() ?: 0
            val selectedOption = when (optionsRadioGroup.checkedRadioButtonId) {
                R.id.rbReservationDate -> "예약일"
                R.id.rbBookingDate -> "예매일"
                else -> "예약일"
            }

            viewModel.addReservation(
                selectedDate,
                hour,
                minute,
                courtNumber,
                selectedOption
            )

            dialog.dismiss()
            updateCalendar()
        }

        dialog.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
