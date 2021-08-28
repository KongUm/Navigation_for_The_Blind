package com.example.nftb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Navigation_1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation1)

        val tv_current_loca = findViewById<TextView>(R.id.tv_currloca)
        tv_current_loca.setText("$lat, $long")

    }
}