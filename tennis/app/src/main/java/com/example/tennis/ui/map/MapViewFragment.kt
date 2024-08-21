package com.example.tennis.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tennis.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapViewFragment : Fragment(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map_view, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // 지도 설정 및 기능 구현
        val latLng = LatLng(37.5665, 126.9780) // 서울 좌표
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))

        // 마커 추가, 줌 제어, 클릭 이벤트 처리 등의 기능 구현
    }
}