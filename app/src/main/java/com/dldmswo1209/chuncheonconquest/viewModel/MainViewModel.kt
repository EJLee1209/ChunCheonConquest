package com.dldmswo1209.chuncheonconquest.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class MainViewModel(application: Application): AndroidViewModel(application) {
    val context = application.applicationContext
    val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("uid", "").toString()
    val db = Firebase.database.reference

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private val _cafeList = MutableLiveData<List<TourSpot>>()
    val cafeList : LiveData<List<TourSpot>>
        get() = _cafeList

    private val _tourList = MutableLiveData<List<TourSpot>>()
    val tourList : LiveData<List<TourSpot>>
        get() = _tourList

    private val _restaurantList = MutableLiveData<List<TourSpot>>()
    val restaurantList : LiveData<List<TourSpot>>
        get() = _restaurantList


    fun getUserInfo() = viewModelScope.launch {
        db.child("Users").child(uid).child("information").get().addOnSuccessListener {
            _user.postValue(it.getValue(User::class.java) as User)
        }
    }
    fun getCafeList() = viewModelScope.launch {
        val cafes = mutableListOf<TourSpot>()
        db.child("Tour").child("cafe").get().addOnSuccessListener {
            it.children.forEach {
                val cafe = it.getValue(TourSpot::class.java) as TourSpot
                cafes.add(cafe)
            }
            _cafeList.postValue(cafes)
        }

    }

    fun getTourList() = viewModelScope.launch {
        val tours = mutableListOf<TourSpot>()
        db.child("Tour").child("tour").get().addOnSuccessListener {
            it.children.forEach {
                val cafe = it.getValue(TourSpot::class.java) as TourSpot
                tours.add(cafe)
                Log.d("testt", "getCafeList() ${cafe.name}")
            }
            _tourList.postValue(tours)
        }
    }

    fun getRestaurantList() = viewModelScope.launch {
        val restaurants = mutableListOf<TourSpot>()
        db.child("Tour").child("restaurant").get().addOnSuccessListener {
            it.children.forEach {
                val cafe = it.getValue(TourSpot::class.java) as TourSpot
                restaurants.add(cafe)
            }
            _restaurantList.postValue(restaurants)
        }
    }
}