package com.example.plantdiary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantdiary.Calendar
import com.example.plantdiary.PlantInfo
import com.example.plantdiary.databinding.FragmentDiaryBinding

class DiaryFragment :Fragment() {

    private var mBinding : FragmentDiaryBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDiaryBinding.inflate(inflater, container,false)

        binding.btnDiary.setOnClickListener {
            val intent = Intent(this@DiaryFragment.requireContext(), Calendar::class.java)
            startActivity(intent)
        }

        mBinding = binding

        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }
}
