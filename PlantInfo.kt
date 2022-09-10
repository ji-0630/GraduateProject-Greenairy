package com.example.plantdiary

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_plant_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class PlantInfo : AppCompatActivity() {

    val arr = arrayListOf<plant>()
    val display = arrayListOf<plant>()
    val adpt = resultAdapter(this, display)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_info)

        var json: String? = null
        val inputStream: InputStream = assets.open("inPlant.json")
        json = inputStream.bufferedReader().use { it.readText() }

        val jsonarr = JSONArray(json)

        for (i in 0..jsonarr.length() - 1) {
            var jsonobj = jsonarr.getJSONObject(i)
            // var cntnum = jsonobj.getInt("cntntsNo").toString()

            var forplant = plant(
                jsonobj.getString("mainImgUrl1"),
                jsonobj.getString("cntntsSj"),
                jsonobj.getString("managelevelCodeNm"),
                jsonobj.getString("fncltyInfo"),
                jsonobj.getString("watercycleAutumnCode"),
                jsonobj.getString("lighttdemanddoCodeNm"),
                jsonobj.getString("postngplaceCodeNm")
            )
            arr.add(forplant)
        }
        display.addAll(arr)

        //val adpt = resultAdapter(this, display)
        //val adapter = PlantRecyclerviewAdapter(arr)

        json_list2.adapter = adpt

        toMainPage.setOnClickListener {
            moveMainPage()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.plant_menu, menu)
        val menuItem = menu!!.findItem(R.id.search)

        if (menuItem != null) {

            val searchview = menuItem.actionView as SearchView

            searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText!!.isNotEmpty()){
                        display.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        arr.forEach {
                            if(it.func.toLowerCase(Locale.getDefault()).contains(search)
                                or it.name.toLowerCase(Locale.getDefault()).contains(search)
                                or it.water.toLowerCase(Locale.getDefault()).contains(search)
                                or it.sun.toLowerCase(Locale.getDefault()).contains(search)
                                or it.manag.toLowerCase(Locale.getDefault()).contains(search)
                            ){
                                display.add(it)
                            }
                        }
                        adpt!!.notifyDataSetChanged()
                    }
                    else{
                        display.clear()
                        display.addAll(arr)
                        adpt!!.notifyDataSetChanged()
                    }
                    return true
                }

            })

        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


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
