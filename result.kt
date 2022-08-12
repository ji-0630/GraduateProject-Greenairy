package com.example.plantdiary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStream
import java.net.Socket
import kotlin.concurrent.thread

class Result : AppCompatActivity() {

    var arr = arrayListOf<plant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        //최종 결과 받아옴
        val answer = intent.getIntegerArrayListExtra("finalResult")
        val data = answer.toString()
        var recvData = ""

            //파이썬과 소켓 통신
            thread(start = true){
            try {
                val socket = Socket("내 아이피 주소", "포트 넘버")
                val outputStream = socket.getOutputStream()
                val inputStream = socket.getInputStream()

                outputStream.write(data.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                Log.d("태그 flush", "flush 완료되었음")

                var isRead = false
                while(!isRead){
                    if (inputStream.available() > 0) {
                        isRead = true
                    }
                    inputStream.bufferedReader(Charsets.UTF_8).forEachLine {
                        recvData = it
                        Log.d("태그 input", it)
                    }
                    isRead = true
                }
                if(isRead){
                    outputStream.close()
                    inputStream.close()
                    socket.close()
                }
                Log.d("태그", "${recvData.toString()}")

            } catch (e: Exception) {
                e.printStackTrace()

            }

        }
            
        //소켓통신으로 받아온 데이터 plant api와 연결하여 추천된 식물 출력

        var json : String? = null
        val inputStream: InputStream = assets.open("inPlant.json")
        json = inputStream.bufferedReader().use { it.readText() }

        val jsonarr = JSONArray(json)

        for (i in 0..jsonarr.length()-1)
        {
            var jsonobj = jsonarr.getJSONObject(i)
            var cntnum = jsonobj.getInt("cntntsNo").toString()

            if (cntnum in recvData) {

                var forplant = plant(jsonobj.getString("mainImgUrl1"),jsonobj.getString("cntntsSj"),
                    jsonobj.getString("managelevelCodeNm"),jsonobj.getString("fncltyInfo"),
                    jsonobj.getString("watercycleAutumnCode"),jsonobj.getString("lighttdemanddoCodeNm"),
                    jsonobj.getString("postngplaceCodeNm"))
                arr.add(forplant)
            }

        }


        var adpt = resultAdapter(this, arr)
        json_list.adapter = adpt

        toMainPage.setOnClickListener{
            moveMainPage()
        }
    }

  /*결과 띄워주는 화면에 대한 Adapter*/
    inner class resultAdapter(val context: Context, val plantList: ArrayList<plant>): BaseAdapter(){
        override fun getCount(): Int {
            return plantList.size
        }

        override fun getItem(position: Int): Any {
            return plantList[position]
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View = LayoutInflater.from(context).inflate(R.layout.list_result, null)

            val img = view.findViewById<ImageView>(R.id.custom_list_image)
            val name = view.findViewById<TextView>(R.id.custom_list1)
            val water = view.findViewById<TextView>(R.id.custom_list2)
            val sun = view.findViewById<TextView>(R.id.custom_list3)
            val manag = view.findViewById<TextView>(R.id.custom_list4)
            val func = view.findViewById<TextView>(R.id.custom_list5)

            val plant = plantList[position]

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO){
                    PlantRecyclerviewAdapter.ImageLoader.loadImage(plant.img)
                }
                img.setImageBitmap(bitmap)
            }

            name.text = plant.name

            if(plant.manag != ""){
                manag.text = "[추천대상] \n" + plant.manag
            }
            else
                manag.text = ""

            if(plant.water != ""){
                water.text = "[물주기] \n" + plant.water
            }
            else
                water.text = ""

            if(plant.sun != ""){
                sun.text = "[햇빛 요구도] \n" + plant.sun
            }
            else
                sun.text = ""

            if(plant.func != ""){
                func.text = "[기능정보] \n" + plant.func
            }
            else
                func.text = ""


            return view
        }

    }


    fun moveMainPage(){
        startActivity(Intent(this, MainActivity::class.java))
    }


}

