package com.example.plantdiary

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.plantdiary.databinding.ListPlantBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class PlantRecyclerviewAdapter(private val dataset: plantapi) : RecyclerView.Adapter<PlantRecyclerviewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    object ImageLoader{
        suspend fun loadImage(imageUrl: String): Bitmap?{
            val bmp: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val stream = url.openStream()

                return BitmapFactory.decodeStream(stream)
            } catch (e: MalformedURLException){
                e.printStackTrace()
            } catch (e: IOException){
                e.printStackTrace()
            }
            return bmp
        }
    }


    class ViewHolder(private val binding:ListPlantBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: PlantData) {
            with(binding){

                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO){
                        ImageLoader.loadImage(data.mainImgUrl1)
                    }
                    ivPlant.setImageBitmap(bitmap)
                }

                tvName.text = data.cntntsSj
                tvInfo1.text = data.fncltyInfo
                tvInfo2.text = data.speclmanageInfo
                tvInfo3.text = data.toxctyInfo

            }
        }
    }
}

