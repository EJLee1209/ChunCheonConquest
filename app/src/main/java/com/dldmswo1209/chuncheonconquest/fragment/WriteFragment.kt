package com.dldmswo1209.chuncheonconquest.fragment

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.databinding.FragmentWriteBinding
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.User
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.util.*

class WriteFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentWriteBinding
    private var imageUri : Uri? = null
    private lateinit var userInfo: User
    private val viewModel : MainViewModel by activityViewModels()
    // 이미지를 결과값으로 받는 변수
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
            result ->
        if((result.resultCode == RESULT_OK)){
            // 이미지를 받으면 imageUri 변수에 저장 후 imageView 에 적용
            imageUri = result.data?.data
            Log.d("testt", imageUri?.lastPathSegment.toString())


            imageUri.let {
                Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(binding.imageView)
            }

        }
    }

    companion object{
        // 갤러리 권한 요청
        const val REQ_GALLERY = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 팝업 생성 시 전체화면으로 띄우기
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false // 드래그 금지

        userInfo = (activity as MainActivity).getUserInfo()

        binding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }

        binding.okButton.setOnClickListener {
            var title = binding.titleEditText.text.toString()
            var date = binding.dateTextView.text.toString()
            var content = binding.contentEditText.text.toString()
            var imageUrl = "images/${userInfo.uid}_${imageUri?.lastPathSegment.toString()}.png"

            if(title == "" || date == "" || content == "") {
                Toast.makeText(requireContext(), "내용을 모두 작성해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val post = Post(userInfo.uid,title, imageUrl, content, date)
            viewModel.uploadPost(post, imageUri!!, userInfo)
            dialog?.dismiss()
        }

        binding.imageView.setOnClickListener {
            selectGallery()
        }

        binding.dateTextView.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                binding.dateTextView.text = dateString
            }
            DatePickerDialog(requireContext(), dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }



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


}