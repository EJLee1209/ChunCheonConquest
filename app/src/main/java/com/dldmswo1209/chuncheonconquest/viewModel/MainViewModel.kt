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
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
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
    val db = Firebase.database.reference
    var storageRef = FirebaseStorage.getInstance().reference

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

    // 유저 정보 가져오기
    fun getUserInfo() = viewModelScope.launch {
        db.child("Users").child(uid).child("information").get().addOnSuccessListener {
            _user.postValue(it.getValue(User::class.java) as User)
        }
    }

    // 카페 정보 가져오기
    fun getCafeList() = viewModelScope.launch {
        val cafes = mutableListOf<TourSpot>()
        db.child("ChunCheonTour").child("Cafe").get().addOnSuccessListener {
            it.children.forEach {
                val cafe = it.getValue(TourSpot::class.java) as TourSpot
                cafes.add(cafe)
            }
            _cafeList.postValue(cafes)
        }
    }

    // 관광지 정보 가져오기
    fun getTourList() = viewModelScope.launch {
        val tours = mutableListOf<TourSpot>()
        db.child("ChunCheonTour").child("Tour").get().addOnSuccessListener {
            it.children.forEach {
                val cafe = it.getValue(TourSpot::class.java) as TourSpot
                tours.add(cafe)
            }
            _tourList.postValue(tours)
        }
    }

    // 식당정보 가져오기
    fun getRestaurantList() = viewModelScope.launch {
        val restaurants = mutableListOf<TourSpot>()
        db.child("ChunCheonTour").child("Restaurant").get().addOnSuccessListener {
            it.children.forEach {
                val cafe = it.getValue(TourSpot::class.java) as TourSpot
                restaurants.add(cafe)
            }
            _restaurantList.postValue(restaurants)
        }
    }

    // 프로필 변경
    fun updateUserInfo(user: User, imageUri: Uri?) = viewModelScope.launch {
        if(imageUri == null){
            db.child("Users").child(uid).child("information").setValue(user) // 데이터 갱신
                .addOnSuccessListener {
                    getUserInfo() // 업데이트된 유저 정보 가져오기
                }
        }
        else {
            val update = user
            val imgFileName = "${user.uid}_${imageUri.lastPathSegment.toString()}.png"
            storageRef.child("profile_images").child(user.uid).child(imgFileName).putFile(imageUri)
                .addOnSuccessListener { // 이미지 업로드
                    storageRef.child(user.imageUrl.toString()).downloadUrl.addOnSuccessListener { // 이미지 다운로드
                        update.imageUri = it.toString() // 다운로드 받은 이미지 uri 를 업데이트할 객체에 넣어줌
                        db.child("Users").child(uid).child("information").setValue(update) // 데이터 갱신
                            .addOnSuccessListener {
                                getUserInfo() // 업데이트된 유저 정보 가져오기
                            }
                    }
                }
        }
    }

    // 게시물 업로드
    fun uploadPost(post: Post, imageUri: Uri?) = viewModelScope.launch {
        val dbRef = db.child("Post/${post.user.uid}").push()
        val post_key = dbRef.key
        val newPost = post
        newPost.key = post_key.toString()

        if(imageUri == null){ // 이미지가 없는 경우
            dbRef.setValue(newPost) // 그냥 업로드
        }else{ // 이미지가 있는 경우
            val imgFileName = "${post.user.uid}_${imageUri.lastPathSegment.toString()}.png"
            storageRef.child("images").child(imgFileName).putFile(imageUri) // 이미지 업로드
                .addOnSuccessListener {
                    storageRef.child(post.imageUrl.toString()).downloadUrl.addOnSuccessListener { // 이미지 다운로드
                        newPost.imageUri = it.toString() // 새로운 post 객체에 다운로드 받은 이미지의 uri 를 저장
                        dbRef.setValue(newPost) // post 업로드

                    }
                }
        }
    }

    // 게시물 수정
    fun updatePost(post: Post, imageUri: Uri? = null) = viewModelScope.launch {
        val dbRef = db.child("Post/${post.user.uid}/${post.key}")
        if(imageUri == null)
            dbRef.setValue(post)
        else{
            val newPost = post
            val imgFileName = "${post.user.uid}_${imageUri.lastPathSegment.toString()}.png"
            storageRef.child("images").child(imgFileName).putFile(imageUri) // 이미지 업로드
                .addOnSuccessListener {
                    storageRef.child(post.imageUrl.toString()).downloadUrl.addOnSuccessListener { // 이미지 다운로드
                        newPost.imageUri = it.toString() // 새로운 post 객체에 다운로드 받은 이미지의 uri 를 저장
                        dbRef.setValue(newPost) // post 업로드

                    }
                }
        }
    }

    fun deletePost(post: Post) = viewModelScope.launch {
        val dbRef = db.child("Post/${post.user.uid}/${post.key}").removeValue()
    }


}