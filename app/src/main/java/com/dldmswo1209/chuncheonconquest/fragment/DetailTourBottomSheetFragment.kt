package com.dldmswo1209.chuncheonconquest.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.WebViewActivity
import com.dldmswo1209.chuncheonconquest.adapter.MenuListAdapter
import com.dldmswo1209.chuncheonconquest.databinding.FragmentDetailTourBottomSheetBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons


class DetailTourBottomSheetFragment(val item: TourSpot) : BottomSheetDialogFragment(),
    OnMapReadyCallback {

    private lateinit var binding : FragmentDetailTourBottomSheetBinding
    private val menuListAdapter = MenuListAdapter()
    private lateinit var naverMap: NaverMap
    private lateinit var tourSpot : TourSpot

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        tourSpot = item
        binding = FragmentDetailTourBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        clickEvent()

    }

    private fun clickEvent(){
        binding.linkLayout.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            intent.putExtra("url", item.naverUrl)
            startActivity(intent)
        }

        binding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }

        binding.callLayout.setOnClickListener {
            // Uri를 이용해서 정보 저장
            val myUri = Uri.parse("tel:${tourSpot.tel}")
            // 전환할 정보 설정 - ACTION_DIAL
            val myIntent = Intent(Intent.ACTION_DIAL, myUri)
            // 이동
            startActivity(myIntent)
        }

    }

    private fun initView(){
        // 팝업 생성 시 전체화면으로 띄우기
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false // 드래그 금지


        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
        mapFragment?.getMapAsync(this)

        if (item.img_url != "") {
            Glide.with(binding.root)
                .load(item.img_url)
                .centerCrop()
                .into(binding.tourImageView)
        }
        binding.nameTextView.text = item.title
        binding.infoTextView.text = item.description
        binding.telTextView.text = item.tel
        binding.addressTextView.text = item.addr

        menuListAdapter.submitList(item.menu)
        binding.menuRecyclerView.adapter = menuListAdapter
    }


    override fun onMapReady(map: NaverMap) {
        naverMap = map
        var cameraUpdate = CameraUpdate.scrollTo(LatLng(tourSpot.latitude,tourSpot.longitude))
        map.moveCamera(cameraUpdate)
        map.moveCamera(CameraUpdate.zoomTo(12.0))

        val infoWindow = InfoWindow()
        infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(requireContext()) {
            override fun getText(infoWindow: InfoWindow): CharSequence {
                return tourSpot.title
            }
        }

        val marker = Marker()
        marker.apply {
            position = LatLng(tourSpot.latitude, tourSpot.longitude)
            this.map = naverMap
            width = 70
            height = 90
        }

        infoWindow.open(marker)
    }
}