package com.dldmswo1209.chuncheonconquest.fragment

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.adapter.TourViewPager
import com.dldmswo1209.chuncheonconquest.databinding.FragmentMapBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.android.gms.location.*
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

class MapFragment : Fragment(), OnMapReadyCallback, Overlay.OnClickListener {
    private val permission_request = 99
    private lateinit var binding: FragmentMapBinding
    private lateinit var naverMap: NaverMap
    private val cafeMakerList = mutableListOf<Marker>()
    private val restaurantMarkerList = mutableListOf<Marker>()
    private val tourMarkerList = mutableListOf<Marker>()
    private val cafeList = mutableListOf<TourSpot>()
    private val restaurantList = mutableListOf<TourSpot>()
    private val tourList = mutableListOf<TourSpot>()
    private val viewModel : MainViewModel by activityViewModels()
    private val viewPagerAdapter = TourViewPager()

    private var currentLocation: Location? = null
    //권한 가져오기
    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

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

        binding.tourViewPager.adapter = viewPagerAdapter
        binding.tourViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selected = viewPagerAdapter.currentList[position] // 현재 page 의 호텔 정보를 가져옴
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(selected.latitude, selected.longitude))
                    .animate(CameraAnimation.Easing) // 애니메이션 추가
                naverMap.moveCamera(cameraUpdate)

            }
        })

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

            viewPagerAdapter.submitList(cafeList)
            viewPagerAdapter.notifyDataSetChanged()
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

            viewPagerAdapter.submitList(tourList)
            viewPagerAdapter.notifyDataSetChanged()
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

            viewPagerAdapter.submitList(restaurantList)
            viewPagerAdapter.notifyDataSetChanged()
        }

        binding.locationButton.setOnClickListener {
            currentLocation ?: return@setOnClickListener
            moveCamera(currentLocation!!)

            cafeList.forEach {
                iAmThere(it)
            }
            restaurantList.forEach {
                iAmThere(it)
            }
            tourList.forEach {
                iAmThere(it)
            }

        }
    }

    private fun iAmThere(tour: TourSpot){
        val distance = calDist(currentLocation!!.latitude,currentLocation!!.longitude, tour.latitude, tour.longitude)
        if(distance <= 50) { // 관광지와 현재 위치의 거리가 10미터 이내인 경우
            Log.d("test", "현재 위치와 ${tour.title}의 거리 : ${distance}m")
            (activity as MainActivity).sendNotification(tour) // 알림을 보냄
        }
    }

    fun moveCamera(location: Location){
        val myLocation = LatLng(location.latitude, location.longitude)

        val cameraUpdate = CameraUpdate.scrollTo(myLocation)
        cameraUpdate.animate(CameraAnimation.Fly)
        naverMap.moveCamera(cameraUpdate)
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 5.0
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
                    currentLocation = location
                    Log.d("location: ", "${location.latitude}, ${location.longitude}")
                    // 위치 오버레이의 가시성은 기본적으로 false로 지정되어 있습니다. 가시성을 true로 변경하면 지도에 위치 오버레이가 나타납니다.
                    // 파랑색 점, 현재 위치 표시
                    naverMap.locationOverlay.run {
                        isVisible = true
                        position = LatLng(location.latitude, location.longitude)
                    }

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
        viewModel.getCafeList().observe(this){
            cafeList.clear()
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.RED
                    width = 70
                    height = 90
                    tag = tour.id
                    onClickListener = this@MapFragment
                }
                cafeMakerList.add(marker)
                cafeList.add(tour)
            }
        }

        viewModel.getRestaurantList().observe(this){
            restaurantList.clear()
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.GREEN
                    width = 70
                    height = 90
                    tag = tour.id
                    onClickListener = this@MapFragment
                }
                restaurantMarkerList.add(marker)
                restaurantList.add(tour)
            }
        }

        viewModel.getTourList().observe(this){
            tourList.clear()
            it.forEach { tour ->
                val marker = Marker()
                marker.apply {
                    position = LatLng(tour.latitude, tour.longitude)
                    icon = MarkerIcons.BLACK
                    iconTintColor = Color.BLUE
                    width = 70
                    height = 90
                    tag = tour.id
                    onClickListener = this@MapFragment
                }
                tourMarkerList.add(marker)
                tourList.add(tour)
            }
            binding.tourFloatingButton.performClick()
        }
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

    private fun calDist(lat1:Double, lon1:Double, lat2:Double, lon2:Double) : Long{
        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = sin(radLat1) * sin(radLat2)
        distance += cos(radLat1) * cos(radLat2) * cos(radDist)
        val ret = EARTH_R * acos(distance)

        return Math.round(ret) // 미터 단위
    }

    override fun onClick(overlay: Overlay): Boolean {
        // 마커 클릭 리스너
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overlay.tag // 리스트에 있는 tour id 와 현재 클릭 된 마커의 tag 값을 비교해서 처음으로 같은 경우의 모델을 저장 없으면 null
        }

        selectedModel?.let { // selectedModel 이 null 이 아니면 다음 코드를 실행
            val position = viewPagerAdapter.currentList.indexOf(it) // 리스트에서 selectedModel 의 위치를 찾음
            binding.tourViewPager.currentItem = position // 현재 viewPager 의 page 를 업데이트
        }

        return true
    }
}