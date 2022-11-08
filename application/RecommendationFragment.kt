package com.example.plantdiary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantdiary.Question1
import com.example.plantdiary.R
import com.example.plantdiary.databinding.FragmentRecommendationBinding
import kotlinx.android.synthetic.main.fragment_recommendation.*
import kotlinx.android.synthetic.main.fragment_recommendation.view.*


class RecommendationFragment :Fragment(), View.OnClickListener {

    private var mBinding : FragmentRecommendationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRecommendationBinding.inflate(inflater, container,false)
        mBinding = binding

        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.start ->{
                activity?.let{
                    val next = Intent(context, Question1::class.java).apply {
                        startActivity(this)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }


}
