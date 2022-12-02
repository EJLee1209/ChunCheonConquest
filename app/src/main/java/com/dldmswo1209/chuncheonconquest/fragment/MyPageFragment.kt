package com.dldmswo1209.chuncheonconquest.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.databinding.FragmentMyPageBinding
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel

class MyPageFragment : Fragment() {
    private lateinit var binding: FragmentMyPageBinding
    private lateinit var userInfo: UserInfo
    private val viewModel: MainViewModel by activityViewModels()
    private var imageUri : Uri? = null
    private var isEditMode = false
    // 이미지를 결과값으로 받는 변수
    private lateinit var imageResult: ActivityResultLauncher<Intent>

    companion object{
        // 갤러리 권한 요청
        const val REQ_GALLERY = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userInfo = (activity as MainActivity).getUserInfo()

        showMyProfile()

        binding.editButton.setOnClickListener {
            isEditMode = true
            showUI()
        }

        binding.cancel.setOnClickListener {
            isEditMode = false
            showMyProfile()
            hideUI()
        }

        binding.profileImageView.setOnClickListener {
            if(!isEditMode) return@setOnClickListener

            selectGallery()
        }

        viewModel.getUserInfo().observe(viewLifecycleOwner){
            userInfo = it
            binding.nameEditText.setText(it.name)
            Glide.with(binding.root)
                .load(it.imageUri)
                .circleCrop()
                .into(binding.profileImageView)
        }

        binding.ok.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            // 원래 프사가 있는데 이름만 변경하는 경우
            imageUri = imageUri ?: userInfo.imageUri?.toUri()
            // 원래 프사가 없는데 이름만 변경하는 경우
            if(imageUri == null){
                val userInfo = UserInfo(userInfo.uid, userInfo.email, userInfo.pw, name, null, null)
                viewModel.updateUserInfo(userInfo, null)
            }else{
                val imageUrl = "profile_images/${userInfo.uid}/${userInfo.uid}_${imageUri?.lastPathSegment.toString()}.png"
                val userInfo = UserInfo(userInfo.uid, userInfo.email, userInfo.pw, name, imageUrl, null)
                viewModel.updateUserInfo(userInfo, imageUri)
            }
            isEditMode =false
            hideUI()
        }



    }
    private fun showMyProfile(){
        binding.nameEditText.setText(userInfo.name)
        if(userInfo.imageUri != null) {
            Glide.with(binding.root)
                .load(userInfo.imageUri)
                .circleCrop()
                .into(binding.profileImageView)
        }
    }

    private fun showUI(){
        binding.cameraImageView.visibility = View.VISIBLE
        binding.cancel.visibility = View.VISIBLE
        binding.ok.visibility = View.VISIBLE
        binding.nameEditText.isEnabled = true
    }

    private fun hideUI(){
        binding.cameraImageView.visibility = View.INVISIBLE
        binding.cancel.visibility = View.GONE
        binding.ok.visibility = View.GONE
        binding.nameEditText.isEnabled = false
    }

    // 갤러리를 부르는 메서드
    private fun selectGallery(){
        val writePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        // 권한 확인
        if(writePermission == PackageManager.PERMISSION_DENIED ||
            readPermission == PackageManager.PERMISSION_DENIED){
            // 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions((activity as MainActivity),
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY)
        }else{ // 권한이 있는 경우
            // 갤러리 실행
            val intent = Intent(Intent.ACTION_PICK)
            // intent 의 data와 type 을 동시에 설정하기
            intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )

            imageResult.launch(intent)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        imageResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
                result ->
            if((result.resultCode == Activity.RESULT_OK)){
                // 이미지를 받으면 imageUri 변수에 저장 후 imageView 에 적용
                imageUri = result.data?.data

                Log.d("testt", imageUri.toString())

                imageUri.let {
                    Glide.with(this)
                        .load(imageUri)
                        .circleCrop()
                        .into(binding.profileImageView)
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        isEditMode = false
        Log.d("testt", "onResume")

    }
}