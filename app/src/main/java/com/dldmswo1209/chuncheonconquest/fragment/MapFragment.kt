package com.dldmswo1209.chuncheonconquest.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
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
import com.dldmswo1209.chuncheonconquest.adapter.FindListAdapter
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
    private var placeList = mutableListOf<TourSpot>()
    private var findPlaceList = mutableListOf<TourSpot>()
    private val viewModel : MainViewModel by activityViewModels()
    private val viewPagerAdapter = TourViewPager()
    private var isSearchMode = false
    private var mapFragment: MapFragment? = null
    private lateinit var imm: InputMethodManager
    private var previousMarker: Marker? = null

    private var findAdapter = FindListAdapter{ tour->
        isSearchMode = false
        showOrHideUI()
        allMarkerDelete()
        if(previousMarker != null) deleteMark(previousMarker!!)
        markup(tour)
    }

    private var currentLocation: Location? = null
    //?????? ????????????
    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        binding.searchEditText.requestFocus()
        imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        clickEvent()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isPermitted()) {
            startProcess()
        } else {
            ActivityCompat.requestPermissions((activity as MainActivity), permissions, permission_request)
        }//?????? ??????

        val fm = childFragmentManager
        mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync(this)

        binding.tourViewPager.adapter = viewPagerAdapter
        binding.tourViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selected = viewPagerAdapter.currentList[position] // ?????? page ??? ?????? ????????? ?????????
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(selected.latitude, selected.longitude))
                    .animate(CameraAnimation.Easing) // ??????????????? ??????
                naverMap.moveCamera(cameraUpdate)
            }
        })
    }

    private fun clickEvent(){
        binding.cafeFloatingButton.setOnClickListener{
            // ?????? ????????? ?????? ????????? ????????? ???????????? ???????????? ????????? ??????
            allMarkerDelete()
            cafeMakerList.forEach {
                it.map = naverMap
            }

            viewPagerAdapter.submitList(cafeList)
            viewPagerAdapter.notifyDataSetChanged()
        }

        binding.tourFloatingButton.setOnClickListener {
            // ????????? ????????? ?????? ????????? ???????????? ???????????? ???????????? ????????? ??????
            allMarkerDelete()
            tourMarkerList.forEach {
                it.map = naverMap
            }

            viewPagerAdapter.submitList(tourList)
            viewPagerAdapter.notifyDataSetChanged()
        }

        binding.restaurantFloatingButton.setOnClickListener {
            // ?????? ????????? ?????? ????????? ????????? ???????????? ???????????? ????????? ??????
            allMarkerDelete()
            restaurantMarkerList.forEach {
                it.map = naverMap
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

        binding.searchEditText.setOnClickListener {
            isSearchMode = true
            showOrHideUI()
            viewPagerAdapter.submitList(placeList)
        }

        binding.backButton.setOnClickListener {
            isSearchMode = false
            showOrHideUI()
        }

        binding.searchEditText.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(keyword: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = binding.searchEditText.text.toString()
                findPlaceList.clear()

                if(text.isNullOrBlank()){
                    findAdapter.submitList(placeList)
                }

                placeList.forEach { tour->
                    if(tour.title.contains(text)){
                        findPlaceList.add(tour)
                    }
                }
                findAdapter.submitList(findPlaceList)
                binding.searchRecyclerView.adapter = findAdapter
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun allMarkerDelete(){
        cafeMakerList.forEach {
            it.map = null
        }
        restaurantMarkerList.forEach {
            it.map = null
        }
        tourMarkerList.forEach {
            it.map = null
        }
    }

    private fun showOrHideUI(){
        when(isSearchMode){
            true->{
                binding.searchImage.visibility = View.INVISIBLE
                binding.backButton.visibility = View.VISIBLE
                binding.searchRecyclerView.visibility = View.VISIBLE
                binding.bottomItemLayout.visibility = View.GONE

                placeList = (cafeList + restaurantList + tourList) as MutableList<TourSpot>
                binding.searchRecyclerView.adapter = findAdapter
                findAdapter.submitList(placeList)
            }
            else->{
                binding.backButton.visibility = View.INVISIBLE
                binding.searchImage.visibility = View.VISIBLE
                binding.searchRecyclerView.visibility = View.GONE
                binding.bottomItemLayout.visibility = View.VISIBLE
                hideKeyboard()
            }
        }
    }

    private fun iAmThere(tour: TourSpot){
        val distance = calDist(currentLocation!!.latitude,currentLocation!!.longitude, tour.latitude, tour.longitude)
        if(distance <= 50) { // ???????????? ?????? ????????? ????????? 10?????? ????????? ??????
            Log.d("test", "?????? ????????? ${tour.title}??? ?????? : ${distance}m")
            (activity as MainActivity).sendNotification(tour) // ????????? ??????
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
    //??? ????????? ???????????? ??????
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient //???????????? gps?????? ????????????.
    lateinit var locationCallback: LocationCallback //gps?????? ?????? ????????????.

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListner() {
        val locationRequest = LocationRequest.create()
        locationRequest.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY //?????? ?????????
            interval = 1000 //1?????? ????????? GPS ??????
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for ((i, location) in locationResult.locations.withIndex()) {
                    currentLocation = location
                    Log.d("location: ", "${location.latitude}, ${location.longitude}")
                    // ?????? ??????????????? ???????????? ??????????????? false??? ???????????? ????????????. ???????????? true??? ???????????? ????????? ?????? ??????????????? ???????????????.
                    // ????????? ???, ?????? ?????? ??????
                    naverMap.locationOverlay.run {
                        isVisible = true
                        position = LatLng(location.latitude, location.longitude)
                    }

                }
            }
        }
        //location ?????? ?????? ?????? (locationRequest, locationCallback)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }//???????????? ??????????????? ??????



    override fun onMapReady(map: NaverMap) {
        naverMap = map

        var cameraUpdate = CameraUpdate.scrollTo(LatLng(37.8571641,127.739802))
        map.moveCamera(cameraUpdate)
        map.moveCamera(CameraUpdate.zoomTo(12.0))

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext()) //gps ???????????? ????????????
        setUpdateLocationListner() //???????????? ???????????? ??????

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
    }//????????? ?????? ????????????
    fun startProcess(){
        val fm = (activity as MainActivity).supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            } //??????
        mapFragment.getMapAsync(this)
    } //????????? ????????? onMapReady??????

    private fun calDist(lat1:Double, lon1:Double, lat2:Double, lon2:Double) : Long{
        val EARTH_R = 6371000.0
        val rad = Math.PI / 180
        val radLat1 = rad * lat1
        val radLat2 = rad * lat2
        val radDist = rad * (lon1 - lon2)

        var distance = sin(radLat1) * sin(radLat2)
        distance += cos(radLat1) * cos(radLat2) * cos(radDist)
        val ret = EARTH_R * acos(distance)

        return Math.round(ret) // ?????? ??????
    }

    private fun hideKeyboard(){
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun markup(tour: TourSpot){
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
        marker.map = naverMap

        val placeLocation = LatLng(tour.latitude, tour.longitude)

        val cameraUpdate = CameraUpdate.scrollTo(placeLocation)
        cameraUpdate.animate(CameraAnimation.Fly)
        naverMap.moveCamera(cameraUpdate)
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 5.0

        onClick(marker)
        previousMarker = marker
    }
    private fun deleteMark(marker: Marker){
        marker.map = null
    }

    override fun onClick(overlay: Overlay): Boolean {
        // ?????? ?????? ?????????
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overlay.tag // ???????????? ?????? tour id ??? ?????? ?????? ??? ????????? tag ?????? ???????????? ???????????? ?????? ????????? ????????? ?????? ????????? null
        }

        selectedModel?.let { // selectedModel ??? null ??? ????????? ?????? ????????? ??????
            val position = viewPagerAdapter.currentList.indexOf(it) // ??????????????? selectedModel ??? ????????? ??????
            binding.tourViewPager.currentItem = position // ?????? viewPager ??? page ??? ????????????
        }

        return true
    }



}