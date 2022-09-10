package com.example.plantdiary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.plantdiary.PlantInfo
import com.example.plantdiary.databinding.FragmentPlantBinding

class PlantFragment :Fragment() {

    private var mBinding : FragmentPlantBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPlantBinding.inflate(inflater, container,false)

        binding.btnSearch.setOnClickListener {
            val intent = Intent(this@PlantFragment.requireContext(), PlantInfo::class.java)
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
