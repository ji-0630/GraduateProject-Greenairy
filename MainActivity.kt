package com.example.plantdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.plantdiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        //네비게이션을 담는 호스트
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.my_nav_host) as NavHostFragment

        //네비게이션 컨트롤러
        val navController = navHostFragment.navController

        //바텀 네비게이션 뷰와 네비게이션을 묶어준다.
        NavigationUI.setupWithNavController(mBinding.myBottomNav, navController)

        //외부 저장소에서 읽어올 수 있도록 허가
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

    }
}
