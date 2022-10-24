package com.dldmswo1209.chuncheonconquest.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dldmswo1209.chuncheonconquest.MainActivity
import com.dldmswo1209.chuncheonconquest.R
import com.dldmswo1209.chuncheonconquest.adapter.PostListAdapter
import com.dldmswo1209.chuncheonconquest.databinding.FragmentPostBinding
import com.dldmswo1209.chuncheonconquest.model.Post
import com.dldmswo1209.chuncheonconquest.model.TourSpot
import com.dldmswo1209.chuncheonconquest.viewModel.MainViewModel

class PostFragment : Fragment() {

    private lateinit var binding : FragmentPostBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val postListAdapter = PostListAdapter()
    private lateinit var postList : MutableList<Post>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postList = (activity as MainActivity).getPostList()
        postListAdapter.submitList(postList)
        binding.postRecyclerView.adapter = postListAdapter


        viewModel.postList.observe(viewLifecycleOwner, Observer { postList ->
            postListAdapter.submitList(postList)
            postListAdapter.notifyDataSetChanged()
            Log.d("testt", postList.toString())
        })

        binding.writeButton.setOnClickListener{
            showBottomDialog()
        }
    }

    private fun showBottomDialog(){
        val bottomSheet = WriteFragment()
        bottomSheet.show(parentFragmentManager, bottomSheet.tag)
    }

}