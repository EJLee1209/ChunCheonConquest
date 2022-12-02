package com.dldmswo1209.chuncheonconquest.fragment

import android.os.Bundle
import android.util.Log
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
    private lateinit var postListAdapter : PostListAdapter

    private var postList = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.writeButton.setOnClickListener{
            showBottomDialog(null)
        }

        viewModel.getPost().observe(viewLifecycleOwner){
            postList = it
            postListAdapter = PostListAdapter{ post ->
                showBottomDialog(post)
            }
            postListAdapter.submitList(it)
            binding.postRecyclerView.adapter = postListAdapter

            binding.lottieAnimationView.visibility = View.GONE
        }

        viewModel.getUserInfo().observe(viewLifecycleOwner){ user->
            postList.forEach { post->
                post.user.name = user.name
                post.user.imageUri = user.imageUri
                post.user.imageUrl = user.imageUrl
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

}