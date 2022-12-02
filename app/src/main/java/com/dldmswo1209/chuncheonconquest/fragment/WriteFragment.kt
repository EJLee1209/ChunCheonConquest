package com.dldmswo1209.chuncheonconquest.fragment

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.databinding.FragmentWriteBinding
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.UserInfo
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import java.util.*

class WriteFragment(val post: Post?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentWriteBinding
    private var imageUri : Uri? = null
    private lateinit var userInfo: UserInfo
    private val viewModel : MainViewModel by activityViewModels()
    private var mode = Mode.ORIGINAL

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

    enum class Mode{
        EDIT, ORIGINAL
    }

    companion object{
        // 갤러리 권한 요청
        const val REQ_GALLERY = 1
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) ?: return@setOnShowListener
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED // 다이얼로그를 전체화면으로 보여주기
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true // 드래그시 자동 붕괴
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }
        return bottomSheetDialog
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

        if(post != null){
            // 수정 모드
            mode = Mode.EDIT

            binding.titleEditText.setText(post.title)
            binding.dateTextView.text = post.date
            binding.contentEditText.setText(post.content)
            if(post.imageUri != "") {
                Glide.with(binding.root)
                    .load(post.imageUri)
                    .centerCrop()
                    .into(binding.imageView)
            }

            binding.textView.text = "수정하기"
            binding.cancelButton.text = "삭제"
        }

        userInfo = (activity as MainActivity).getUserInfo()

        binding.cancelButton.setOnClickListener {
            if(mode == Mode.EDIT){ // 수정 모드
                // 게시물 삭제
                post ?: return@setOnClickListener
                viewModel.deletePost(post)
            }
            dialog?.dismiss()
        }

        binding.okButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val date = binding.dateTextView.text.toString()
            val content = binding.contentEditText.text.toString()

            if(title == "" || date == "" || content == "") {
                Toast.makeText(requireContext(), "내용을 모두 작성해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 이미지가 없는 경우
            if(imageUri == null){
                val newPost = Post("",title, null,null, content, date,userInfo)
                if(mode == Mode.ORIGINAL) // 일반 모드
                    viewModel.uploadPost(newPost, null) // 게시물 업로드
                else{ // 수정 모드
                    // 게시물 수정
                    post ?: return@setOnClickListener
                    newPost.key = post.key
                    newPost.imageUri = post.imageUri
                    newPost.imageUrl = post.imageUrl

                    viewModel.updatePost(newPost)
                }
            }
            // 이미지가 있는 경우
            else{
                val imageUrl = "images/${userInfo.uid}_${imageUri?.lastPathSegment.toString()}.png"
                val newPost = Post("",title, imageUrl,null, content, date,userInfo)
                if(mode == Mode.ORIGINAL) // 일반 모드
                    viewModel.uploadPost(newPost, imageUri!!) // 게시물 업로드
                else{
                    // 게시물 수정
                    post ?: return@setOnClickListener
                    newPost.key = post.key
                    viewModel.updatePost(newPost, imageUri)
                }
            }
            dialog?.dismiss()
            (activity as MainActivity).showLottie() // 로딩 애니메이션 보여주기
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