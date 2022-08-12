package com.example.plantdiary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_question2.*

class Question2 : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question2)

        /*Question1에서 보낸 list 받음*/
        val result1 = intent.getIntegerArrayListExtra("environment")
        val result2 = ArrayList<Int>()

        result2.add(0,0)
        result2.add(1,0)
        result2.add(2,0)
        result2.add(3,0)

        /*성향에 따른 질문에 대한 대답을 저장*/
        manageG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_manage1 -> result2[0] = 1
                R.id.rb_manage2 -> result2[0] = 2
            }
        }
        speedG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_speed1 -> result2[1] = 1
                R.id.rb_speed2 -> result2[1] = 2
            }
        }
        smellG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_smell1 -> result2[2] = 1
                R.id.rb_smell2 -> result2[2] = 2
            }
        }
        coldG.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_cold -> result2[3] = 1
                R.id.rb_hot -> result2[3] = 2
            }
        }

         /*두 개의 질문에 대한 답을 한 list로 합치고 최종 결과 창으로 보냄*/
        btn_result.setOnClickListener {
            val joinResult = result1?.plus(result2)

            val resultScreen = Intent(this, Result::class.java)
            resultScreen.putIntegerArrayListExtra("finalResult",
                joinResult as java.util.ArrayList<Int>?
            )
            startActivity(resultScreen)
        }
    }
}

