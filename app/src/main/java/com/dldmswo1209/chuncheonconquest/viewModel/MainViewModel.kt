package com.dldmswo1209.chuncheonconquest.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.Repository.Repo
import com.dldmswo1209.chuncheonconquest.fragment.PostFragment
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.UserData
import com.dldmswo1209.chuncheonconquest.model.UserInfo


class MainViewModel(application: Application): AndroidViewModel(application) {
    val context = application.applicationContext
    val sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    val uid = sharedPreferences.getString("uid", "").toString()
    val repo = Repo()

    fun getAllUser(): LiveData<MutableList<UserData>>{
        val userList = MutableLiveData<MutableList<UserData>>()
        repo.getAllUser().observeForever {
            userList.postValue(it)
        }
        return userList
    }

    // 유저 정보 가져오기
    fun getUserInfo() : LiveData<UserInfo>{
        val userInfo = MutableLiveData<UserInfo>()
        repo.getUserInfo(uid).observeForever {
            userInfo.postValue(it)
        }
        return userInfo
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
    fun updateUserInfo(userInfo: UserInfo, imageUri: Uri?) {
        repo.updateUserInfo(userInfo, imageUri)
    }

    fun getPost() : LiveData<MutableList<Post>> {
        val postList = MutableLiveData<MutableList<Post>>()
        repo.getPost(uid).observeForever {
            postList.postValue(it)
        }
        return postList
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

    fun conquerCountUp(userInfo: UserInfo, tourSpot: TourSpot){
        repo.conquerCountUp(userInfo, tourSpot)
    }

    fun addFriend(friend: UserInfo){
        repo.addFriend(uid, friend)
    }

    fun getMyFriends(): LiveData<MutableList<UserInfo>>{
        val friendList = MutableLiveData<MutableList<UserInfo>>()
        repo.getMyFriends(uid).observeForever {
            friendList.postValue(it)
        }

        return friendList
    }



}