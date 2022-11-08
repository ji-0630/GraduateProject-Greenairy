package com.example.plantdiary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_question1.*


class Question1 :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question1)

        /*결과를 저장할 result list 생성*/
        val result = ArrayList<Int>()

        result.add(0,0)
        result.add(1,0)
        result.add(2,0)
        result.add(3,0)
        result.add(4,0)
        
        /*사용자가 체크한 답에 따라 result list에 저장 */
        sunG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_sun1 -> result[0] = 1
                R.id.rb_sun2 -> result[0] = 2
                R.id.rb_sun3 -> result[0] = 3
            }
        }
        waterG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_water1 -> result[1] = 1
                R.id.rb_water2 -> result[1] = 2
                R.id.rb_water3 -> result[1] = 3
            }
        }
        winterG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_winter1 -> result[2] = 1
                R.id.rb_winter2 -> result[2] = 2
                R.id.rb_winter3 -> result[2] = 3
                R.id.rb_winter4 -> result[2] = 4
            }
        }
        loveG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_love1 -> result[3] = 1
                R.id.rb_love2 -> result[3] = 1
            }
        }
        safeG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_safe1 -> result[4] = 1
                R.id.rb_safe2 -> result[4] = 2 
            }
        }
        /*저장한 리스트와 함께 Question2 페이지로 넘어감*/
        btn_next.setOnClickListener {
            val nextPage = Intent(this, Question2::class.java)
            nextPage.putIntegerArrayListExtra("environment", result)
            startActivity(nextPage)
        }
    }
}
