package com.dldmswo1209.chuncheonconquest.fragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.FragmentMapBinding
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MapFragment : Fragment(), OnMapReadyCallback {
    private val permission_request = 99
    private lateinit var binding: FragmentMapBinding
    private lateinit var naverMap: NaverMap
    private val cafeMakerList = mutableListOf<Marker>()
    private val restaurantMarkerList = mutableListOf<Marker>()
    private val tourMarkerList = mutableListOf<Marker>()
    //권한 가져오기
    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

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
        if (isPermitted()) {
            startProcess()
        } else {
            ActivityCompat.requestPermissions((activity as MainActivity), permissions, permission_request)
        }//권한 확인

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync(this)

        clickEvent()
    }

    private fun clickEvent(){
        binding.cafeFloatingButton.setOnClickListener{
            // 카페 플로팅 버튼 클릭시 카페에 해당하는 위치에만 마커를 찍음
            cafeMakerList.forEach {
                it.map = naverMap
            }
            restaurantMarkerList.forEach {
                it.map = null
            }
            tourMarkerList.forEach {
                it.map = null
            }
        }

        binding.tourFloatingButton.setOnClickListener {
            // 관광지 플로팅 버튼 클릭시 관광지에 해당하는 위치에만 마커를 찍음
            cafeMakerList.forEach {
                it.map = null
            }
            restaurantMarkerList.forEach {
                it.map = null
            }
            tourMarkerList.forEach {
                it.map = naverMap
            }
        }

        binding.restaurantFloatingButton.setOnClickListener {
            // 식당 플로팅 버튼 클릭시 식당에 해당하는 위치에만 마커를 찍음
            cafeMakerList.forEach {
                it.map = null
            }
            restaurantMarkerList.forEach {
                it.map = naverMap
            }
            tourMarkerList.forEach {
                it.map = null
            }
        }
    }

    fun setLastLocation(location: Location) {
        val myLocation = LatLng(location.latitude, location.longitude)

        val cameraUpdate = CameraUpdate.scrollTo(myLocation)
        naverMap.moveCamera(cameraUpdate)
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 5.0
        // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
        // 파랑색 점, 현재 위치 표시
        naverMap.locationOverlay.run {
            isVisible = true
            position = LatLng(location!!.latitude, location!!.longitude)
        }

        //marker.map = null
    }
    //내 위치를 가져오는 코드
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient //자동으로 gps값을 받아온다.
    lateinit var locationCallback: LocationCallback //gps응답 값을 가져온다.
    //lateinit: 나중에 초기화 해주겠다는 의미

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListner() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY //높은 정확도
            interval = 1000 //1초에 한번씩 GPS 요청
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for ((i, location) in locationResult.locations.withIndex()) {
                    Log.d("location: ", "${location.latitude}, ${location.longitude}")
                    setLastLocation(location)
                }
            }
        }
        //location 요청 함수 호출 (locationRequest, locationCallback)

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }//좌표계를 주기적으로 갱신



    override fun onMapReady(map: NaverMap) {
        naverMap = map
        var cameraUpdate = CameraUpdate.scrollTo(LatLng(37.8571641,127.739802))
        map.moveCamera(cameraUpdate)
        map.moveCamera(CameraUpdate.zoomTo(12.0))


        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext()) //gps 자동으로 받아오기
        setUpdateLocationListner() //내위치를 가져오는 코드

        getMarker()

    }
    fun getMarker(){
        viewModel.getCafeList()
        viewModel.cafeList.observe(viewLifecycleOwner, Observer {
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.RED
                    width = 70
                    height = 90
                }
                cafeMakerList.add(marker)

            }
        })

        viewModel.getRestaurantList()
        viewModel.restaurantList.observe(viewLifecycleOwner, Observer {
            it.forEach { rest ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(rest.latitude, rest.longitude)
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.GREEN
                    width = 70
                    height = 90
                }
                restaurantMarkerList.add(marker)
            }
        })

        viewModel.getTourList()
        viewModel.tourList.observe(viewLifecycleOwner, Observer {
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.BLUE
                    width = 70
                    height = 90
                }
                tourMarkerList.add(marker)
            }
        })
    }

    fun isPermitted(): Boolean {
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }//권한을 허락 받아야함
    fun startProcess(){
        val fm = (activity as MainActivity).supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            } //권한
        mapFragment.getMapAsync(this)
    } //권한이 있다면 onMapReady연결
}