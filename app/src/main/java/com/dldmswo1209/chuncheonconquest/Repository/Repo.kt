package com.dldmswo1209.chuncheonconquest.Repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.model.UserData
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class Repo {
    val db = Firebase.database.reference
    var storageRef = FirebaseStorage.getInstance().reference

    fun getAllUser(): LiveData<MutableList<UserData>> {
        val userInfoList = MutableLiveData<MutableList<UserData>>()

        db.child("Users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<UserData>()
                snapshot.children.forEach {
                    val data = it.getValue(UserData::class.java) ?: return@forEach
                    dataList.add(data)
                }
                userInfoList.postValue(dataList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return userInfoList
    }

    // 유저 정보 가져오기
    fun getUserInfo(uid: String): LiveData<UserInfo> {
        val userInfo = MutableLiveData<UserInfo>()

        db.child("Users/${uid}/information").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userInfo.postValue(snapshot.getValue(UserInfo::class.java))
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        return userInfo
    }

    // 모든 카페 목록
    fun getCafeList() : LiveData<MutableList<TourSpot>> {
        val cafeList = MutableLiveData<MutableList<TourSpot>>()

        db.child("ChunCheonTour/Cafe").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<TourSpot>()
                snapshot.children.forEach {
                    val data = it.getValue(TourSpot::class.java) as TourSpot
                    dataList.add(data)
                }
                cafeList.postValue(dataList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return cafeList
    }

    // 모든 식당 목록
    fun getRestaurantList(): LiveData<MutableList<TourSpot>> {
        val restaurantList = MutableLiveData<MutableList<TourSpot>>()

        db.child("ChunCheonTour/Restaurant").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<TourSpot>()
                snapshot.children.forEach {
                    val data = it.getValue(TourSpot::class.java) as TourSpot
                    dataList.add(data)
                }
                restaurantList.postValue(dataList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return restaurantList
    }

    // 모든 관광지 목록
    fun getTourList(): LiveData<MutableList<TourSpot>>{
        val tourList = MutableLiveData<MutableList<TourSpot>>()

        db.child("ChunCheonTour/Tour").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = mutableListOf<TourSpot>()
                snapshot.children.forEach {
                    val data = it.getValue(TourSpot::class.java) as TourSpot
                    dataList.add(data)
                }
                tourList.postValue(dataList)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return tourList
    }

    // 프로필 변경
    fun updateUserInfo(userInfo: UserInfo, imageUri: Uri?) {
        if(imageUri == null){
            db.child("Users").child(userInfo.uid).child("information").setValue(userInfo) // 데이터 갱신
        }
        else {
            val update = userInfo
            val imgFileName = "${userInfo.uid}_${imageUri.lastPathSegment.toString()}.png"
            storageRef.child("profile_images").child(userInfo.uid).child(imgFileName).putFile(imageUri)
                .addOnSuccessListener { // 이미지 업로드
                    storageRef.child(userInfo.imageUrl.toString()).downloadUrl.addOnSuccessListener { // 이미지 다운로드
                        update.imageUri = it.toString() // 다운로드 받은 이미지 uri 를 업데이트할 객체에 넣어줌
                        db.child("Users").child(userInfo.uid).child("information").setValue(update) // 데이터 갱신
                    }
                }
        }
    }

    // 게시물 업로드
    fun uploadPost(post: Post, imageUri: Uri?) {
        val dbRef = db.child("Post/${post.userInfo.uid}").push()
        val post_key = dbRef.key
        val newPost = post
        newPost.key = post_key.toString()

        if(imageUri == null){ // 이미지가 없는 경우
            dbRef.setValue(newPost) // 그냥 업로드
        }else{ // 이미지가 있는 경우
            val imgFileName = "${post.userInfo.uid}_${imageUri.lastPathSegment.toString()}.png"
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
    fun updatePost(post: Post, imageUri: Uri? = null) {
        val dbRef = db.child("Post/${post.userInfo.uid}/${post.key}")
        if(imageUri == null)
            dbRef.setValue(post)
        else{
            val newPost = post
            val imgFileName = "${post.userInfo.uid}_${imageUri.lastPathSegment.toString()}.png"
            storageRef.child("images").child(imgFileName).putFile(imageUri) // 이미지 업로드
                .addOnSuccessListener {
                    storageRef.child(post.imageUrl.toString()).downloadUrl.addOnSuccessListener { // 이미지 다운로드
                        newPost.imageUri = it.toString() // 새로운 post 객체에 다운로드 받은 이미지의 uri 를 저장
                        dbRef.setValue(newPost) // post 업로드

                    }
                }
        }
    }

    // 게시물 삭제
    fun deletePost(post: Post) {
        db.child("Post/${post.userInfo.uid}/${post.key}").removeValue()
    }

    // 정복 카운트 증가 시키기
    fun conquerCountUp(userInfo: UserInfo, tourSpot: TourSpot) {
        db.child("Users/${userInfo.uid}/conquerSpot/${tourSpot.title}").setValue(tourSpot)
            .addOnSuccessListener {
                db.child("Users/${userInfo.uid}/conquerSpot").get().addOnSuccessListener {
                    val count = it.childrenCount
                    db.child("Users/${userInfo.uid}/conquerCount").setValue(count)
                }
            }
    }

    fun addFriend(uid: String, friend: UserInfo){
        db.child("Users/${uid}/friends/${friend.uid}").setValue(friend)
    }

    fun getMyFriends(uid: String): LiveData<MutableList<UserInfo>>{
        val friendList = MutableLiveData<MutableList<UserInfo>>()
        db.child("Users/${uid}/friends").addValueEventListener(object: ValueEventListener{
            val dataList = mutableListOf<UserInfo>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    snapshot.children.forEach {
                        val data = it.getValue(UserInfo::class.java) as UserInfo
                        dataList.add(data)
                    }
                }
                friendList.postValue(dataList)
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        return friendList
    }
}