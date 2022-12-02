package com.dldmswo1209.chuncheonconquest.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.adapter.PostListAdapter
import com.dldmswo1209.chuncheonconquest.databinding.FragmentPostBinding
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostFragment : Fragment() {

    private lateinit var binding : FragmentPostBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val postListAdapter = PostListAdapter{ post ->
        showBottomDialog(post)
    }
    private val postList = mutableListOf<Post>()
    private val eventListener = object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            binding.lottieAnimationView.visibility = View.VISIBLE // 데이터를 가져오는 동안 로딩 애니메이션을 보여줌
            postList.clear()
            snapshot.children.forEach {
                val post = it.getValue(Post::class.java) as Post
                postList.add(0,post)
            }
            postListAdapter.submitList(postList)
            postListAdapter.notifyDataSetChanged()

            binding.lottieAnimationView.visibility = View.GONE
        }

        override fun onCancelled(error: DatabaseError) {

        }

    }
    private lateinit var db : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userInfo = (activity as MainActivity).getUserInfo()
        db = Firebase.database.reference.child("Post/${userInfo.uid}")
        db.addValueEventListener(eventListener)

        binding.postRecyclerView.adapter = postListAdapter

        binding.writeButton.setOnClickListener{
            showBottomDialog(null)
        }

        viewModel.getUserInfo().observe(viewLifecycleOwner){ user->
            postList.forEach { post->
                post.userInfo.name = user.name
                post.userInfo.imageUri = user.imageUri
                post.userInfo.imageUrl = user.imageUrl
                viewModel.updatePost(post)
            }
        }
    }

    private fun showBottomDialog(post: Post?){
        val bottomSheet = WriteFragment(post)
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }
    fun showLottie(){
        binding.lottieAnimationView.visibility = View.VISIBLE
    }

    override fun onDetach() {
        super.onDetach()
        db.removeEventListener(eventListener)
    }

}