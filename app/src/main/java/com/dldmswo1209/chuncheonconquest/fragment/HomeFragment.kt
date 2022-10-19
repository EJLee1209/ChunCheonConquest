package com.dldmswo1209.chuncheonconquest.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.dldmswo1209.chuncheonconquest.adapter.HomeBannerAdapter
import com.dldmswo1209.chuncheonconquest.adapter.TourListAdapter
import com.dldmswo1209.chuncheonconquest.databinding.FragmentHomeBinding
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel : MainViewModel by activityViewModels()

    // 인덱스값이 아이템 수의 절반, 딱 중간쯤에서 시작하도록 해 앞뒤 어디로 이동해도 무한대처럼 보이게 함
    private var bannerPosition = Int.MAX_VALUE/2
    private var homeBannerHandler = HomeBannerHandler()
    // 1.5 초 간격으로 배너 페이지 넘어감
    private val intervalTime = 2000.toLong()

    private val bannerAdapter = HomeBannerAdapter{
        showBottomDialog(it)
    }
    private val tourListAdapter = TourListAdapter {
        showBottomDialog(it)
    }
    private val cafeListAdapter = TourListAdapter{
        showBottomDialog(it)
    }
    private val restaurantListAdapter = TourListAdapter{
        showBottomDialog(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUserInfo()
        viewModel.getCafeList()
        viewModel.getTourList()
        viewModel.getRestaurantList()

        binding.cafeRecyclerView.adapter = cafeListAdapter
        binding.tourRecyclerView.adapter = tourListAdapter
        binding.restaurantRecyclerView.adapter = restaurantListAdapter

        viewModel.user.observe(viewLifecycleOwner, Observer {
            binding.nameTextView.text = "${it.name} 님"
        })

        viewModel.cafeList.observe(viewLifecycleOwner, Observer {
            bannerAdapter.submitList(it)
            binding.viewPager.adapter = bannerAdapter
            binding.viewPager.setCurrentItem(bannerPosition, false)

            cafeListAdapter.submitList(it)
            cafeListAdapter.notifyDataSetChanged()
        })

        viewModel.tourList.observe(viewLifecycleOwner, Observer {
            tourListAdapter.submitList(it)
            tourListAdapter.notifyDataSetChanged()
        })

        viewModel.restaurantList.observe(viewLifecycleOwner, Observer {
            restaurantListAdapter.submitList(it)
            restaurantListAdapter.notifyDataSetChanged()
        })

        binding.viewPager.apply {
            clipToOutline = true
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                //이 메서드의 state 값으로 뷰페이저의 상태를 알 수 있음
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        //뷰페이저가 움직이는 중일 때 자동 스크롤 시작 함수 호출
                        ViewPager2.SCROLL_STATE_DRAGGING -> autoScrollStop()
                        //뷰페이저에서 손 뗐을 때, 뷰페이저가 멈춰있을 때 자동 스크롤 멈춤 함수 호출
                        ViewPager2.SCROLL_STATE_IDLE -> autoScrollStart(intervalTime)
                    }
                }
            })
        }
    }

    private fun showBottomDialog(item: TourSpot){
        val bottomSheet = DetailTourBottomSheetFragment(item)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

    //배너 자동 스크롤 시작하게 하는 함수
    private fun autoScrollStart(intervalTime: Long){
        homeBannerHandler.removeMessages(0) //이거 안하면 핸들러가 여러개로 계속 늘어남
        homeBannerHandler.sendEmptyMessageDelayed(0, intervalTime) //intervalTime만큼 반복해서 핸들러를 실행
    }

    //배너 자동 스크롤 멈추게 하는 함수
    private fun autoScrollStop(){
        homeBannerHandler.removeMessages(0) //핸들러 중지
    }

    //배너 자동 스크롤 컨트롤하는 클래스
    private inner class HomeBannerHandler: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == 0){
                binding.viewPager.setCurrentItem(++bannerPosition, true) //다음 페이지로 이동
                autoScrollStart(intervalTime) //스크롤 킵고잉
            }
        }
    }

    //다른 화면으로 갔다가 돌아오면 배너 스크롤 다시 시작
    override fun onResume() {
        super.onResume()
        autoScrollStart(intervalTime)
    }

    //다른 화면을 보고 있는 동안에는 배너 스크롤 안함
    override fun onPause() {
        super.onPause()
        autoScrollStop()
    }
}