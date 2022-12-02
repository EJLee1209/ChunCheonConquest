package com.dldmswo1209.chuncheonconquest.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.Repository.Repo
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainViewModel(application: Application): AndroidViewModel(application) {
    val context = application.applicationContext
    val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("uid", "").toString()
    val repo = Repo()

    // 유저 정보 가져오기
    fun getUserInfo() : LiveData<User>{
        val user = MutableLiveData<User>()
        repo.getUserInfo(uid).observeForever {
            user.postValue(it)
        }
        return user
    }

    // 카페 정보 가져오기
    fun getCafeList() : LiveData<MutableList<TourSpot>> {
        val cafeList = MutableLiveData<MutableList<TourSpot>>()
        repo.getCafeList().observeForever {
            cafeList.postValue(it)
        }
        return cafeList
    }

    // 관광지 정보 가져오기
    fun getTourList() : LiveData<MutableList<TourSpot>>{
        val tourList = MutableLiveData<MutableList<TourSpot>>()
        repo.getTourList().observeForever {
            tourList.postValue(it)
        }
        return tourList
    }

    // 식당정보 가져오기
    fun getRestaurantList() : LiveData<MutableList<TourSpot>>{
        val restaurantList = MutableLiveData<MutableList<TourSpot>>()
        repo.getRestaurantList().observeForever {
            restaurantList.postValue(it)
        }
        return restaurantList
    }

    // 프로필 변경
    fun updateUserInfo(user: User, imageUri: Uri?) {
        repo.updateUserInfo(user, imageUri)
    }

    // 게시물 업로드
    fun uploadPost(post: Post, imageUri: Uri?)  {
        repo.uploadPost(post, imageUri)
    }

    // 게시물 수정
    fun updatePost(post: Post, imageUri: Uri? = null) {
        repo.updatePost(post, imageUri)
    }

    fun deletePost(post: Post) {
        repo.deletePost(post)
    }

    fun conquerCountUp(user: User, tourSpot: TourSpot){
        repo.conquerCountUp(user, tourSpot)
    }



}