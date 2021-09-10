package com.example.nftb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Navigation_4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation4)

        val TvArrive = findViewById<TextView>(R.id.tv_address)
        val BtnExit2 = findViewById<Button>(R.id.btn_exit_2)
        TvArrive.setText("도착: $Destination_info")

        BtnExit2.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            //TODO: Navigation 3,4 종료 = 리셋
        }
    }
}