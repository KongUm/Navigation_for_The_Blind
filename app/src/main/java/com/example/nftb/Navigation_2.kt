package com.example.nftb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

var Destination_info = ""
val Dest_lat = mutableListOf<String>()
val Dest_long = mutableListOf<String>()

var Destination_lat = ""
var Destination_long = ""


class Navigation_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation2)

        val Dest_list = mutableListOf<String>()


        val Tvaddress = findViewById<TextView>(R.id.tv_address)
        Tvaddress.setText("검색어: $start_address")

        for (i in Dest_name.indices) {
            if(Dest_name[i].replace(" ","") == start_address?.replace(" ","") || Dest_address[i].replace(" ","") == start_address?.replace(" ","")){
                Dest_list.add("지명: ${Dest_name[i]}\n주소: ${Dest_address[i]}")
                Dest_lat.add("${Dest_latitude[i].toString()}")
                Dest_long.add("${Dest_longitude[i].toString()}")
            }
        }

        val list_view = findViewById<ListView>(R.id.lv_search)
        list_view.adapter = ArrayAdapter(this, R.layout.search_result, Dest_list)

        list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Destination_info = "${Dest_list[position]}"
            Destination_lat = "${Dest_lat[position]}"
            Destination_long = "${Dest_long[position]}"

            Log.d("locat", Dest_lat[position])
            Log.d("locat", Dest_long[position])

            val intent = Intent(this, Navigation_3::class.java)
            startActivity(intent)
        }
    }
}



