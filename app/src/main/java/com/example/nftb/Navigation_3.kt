package com.example.nftb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Navigation_3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation3)

        val Tvdest = findViewById<TextView>(R.id.tv_dest)
        Tvdest.setText("목적지\n$Destination_info")
    }
}

