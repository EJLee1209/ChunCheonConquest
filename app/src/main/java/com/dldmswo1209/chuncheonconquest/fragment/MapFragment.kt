package com.dldmswo1209.chuncheonconquest.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.FragmentMapBinding
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private lateinit var naverMap: NaverMap
    private val viewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map
        var cameraUpdate = CameraUpdate.scrollTo(LatLng(37.8571641,127.739802))
        map.moveCamera(cameraUpdate)
        map.moveCamera(CameraUpdate.zoomTo(12.0))

        viewModel.getCafeList()
        viewModel.cafeList.observe(viewLifecycleOwner, Observer {
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    this.map = naverMap
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.RED
                    width = 70
                    height = 90
                }
            }
        })

        viewModel.getRestaurantList()
        viewModel.restaurantList.observe(viewLifecycleOwner, Observer {
            it.forEach { rest ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(rest.latitude, rest.longitude)
                    this.map = naverMap
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.GREEN
                    width = 70
                    height = 90
                }
            }
        })

        viewModel.getTourList()
        viewModel.tourList.observe(viewLifecycleOwner, Observer {
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    this.map = naverMap
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.BLUE
                    width = 70
                    height = 90
                }

            }
        })

    }
}