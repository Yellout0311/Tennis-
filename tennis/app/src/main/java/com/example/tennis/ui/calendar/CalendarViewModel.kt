package com.example.tennis.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalendarViewModel : ViewModel() {
    // 예약 정보를 저장할 리스트
    private val _reservations = MutableLiveData<List<Reservation>>()
    val reservations: LiveData<List<Reservation>> = _reservations

    init {
        _reservations.value = emptyList()
    }

    fun addReservation(date: String, time: String, details: String) {
        val currentList = _reservations.value.orEmpty().toMutableList()
        currentList.add(Reservation(date, time, details))
        currentList.sortBy { it.date }
        _reservations.value = currentList
    }

    // 예약 정보를 나타내는 데이터 클래스
    data class Reservation(val date: String, val time: String, val details: String)
}

