package com.dldmswo1209.chuncheonconquest.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dldmswo1209.chuncheonconquest.FriendActivity
import com.dldmswo1209.chuncheonconquest.databinding.FragmentAddFriendBottomBinding
import com.dldmswo1209.chuncheonconquest.model.UserData
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFriendBottomFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentAddFriendBottomBinding
    private var allUser = mutableListOf<UserData>()
    private val viewModel : MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED // bottomSheetDialog 가 완전히 펼쳐진 상태로 보여지게 됨
            behavior.skipCollapsed = true // 드래그하면 dialog 가 바로 닫힘
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddFriendBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allUser = (activity as FriendActivity).allUser

        binding.addFriendButton.setOnClickListener {
            val email = binding.friendIdEditText.text.toString()
            allUser.forEach { userData ->
                if(userData.information.email == email){
                    Log.d("testt", "find friend! : ${userData.information.name}")
                    val friendInfo = userData.information
                    viewModel.addFriend(friendInfo) // 친구 추가
                    return@forEach
                }
            }
        }

        binding.closeButton.setOnClickListener {
            dialog?.dismiss()
        }



    }
}