package com.example.nftb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*


class Navigation_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation2)

        val Dest_list = mutableListOf<String>()

        for (i in Dest_name.indices) {
            if(Dest_name[i].trimIndent() == start_address || Dest_address[i].trimIndent() == start_address){
                Dest_list.add(i, "지명: ${Dest_name[i]}\n" +
                        "주소: ${Dest_address[i]}")
            }else{}
        }
            val list_view = findViewById<ListView>(R.id.lv_search)
            list_view.adapter = ArrayAdapter(this, R.layout.search_result, Dest_list)

        list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val data = PostModel("$start_long", "$start_lat", "${Dest_longitude[position]}", "${Dest_latitude[position]}")
            Log.d("log", Dest_longitude[position])
            Log.d("log", Dest_latitude[position])

            val tvt = findViewById<TextView>(R.id.tv_t)
            tvt.setText("${Dest_longitude[position]},${Dest_latitude[position]}")

            val intent = Intent(this, Navigation_3::class.java)
            startActivity(intent)
        }
    }
}



