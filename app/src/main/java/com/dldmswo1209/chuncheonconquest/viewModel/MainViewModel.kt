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
    var storageRef = FirebaseStorage.getInstance().reference.child("images")

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

    private val _postList = MutableLiveData<List<Post>>()
    val postList : LiveData<List<Post>>
        get() = _postList


    fun getUserInfo() = viewModelScope.launch {
        db.child("Users").child(uid).child("information").get().addOnSuccessListener {
            _user.postValue(it.getValue(User::class.java) as User)
        }
    }
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

    private fun uploadImage(imageUri: Uri, userInfo: User) = viewModelScope.launch {
        val imgFileName = "${userInfo.uid}_${imageUri.lastPathSegment.toString()}.png"
        storageRef.child(imgFileName).putFile(imageUri).addOnSuccessListener {
            Log.d("testt", "ok")
        }
    }

    fun uploadPost(post: Post, imageUri: Uri, userInfo: User) = viewModelScope.launch {
        db.child("Post").child(userInfo.uid).child("${post.title}_${post.date}").setValue(post)
        uploadImage(imageUri, userInfo)
    }

    fun getPost(userInfo: User) = viewModelScope.launch {
        val posts = mutableListOf<Post>()
        var count = 0
        db.child("Post").child(userInfo.uid).get().addOnSuccessListener { dataSnapshot ->
            dataSnapshot.children.forEach { data ->
                val post = data.getValue(Post::class.java) as Post
                FirebaseStorage.getInstance().reference.child(post.imageUrl.toString()).downloadUrl.addOnSuccessListener { // 이미지 uri 가져오기
                    post.imageUri = it.toString()
                    posts.add(post)
                    count+=1
                }
                    .addOnCompleteListener {
                        if(count == dataSnapshot.childrenCount.toInt()) // 모든 게시물을 가져왔으면
                            _postList.postValue(posts) // 업데이트
                    }
            }
        }
    }



}