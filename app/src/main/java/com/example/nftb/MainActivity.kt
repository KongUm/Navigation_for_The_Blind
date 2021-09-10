package com.example.nftb


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_m1 = findViewById<Button>(R.id.btn_main_1)

        btn_m1.setOnClickListener {

            GpsTracker(this, this)

            if (start_latitude != "" && start_longitude != ""){
                val intent = Intent(this, Navigation_1::class.java)
                startActivity(intent) //보행 내비게이션 버튼 누를 시 Navigation_1로 이동
            }else{
                return@setOnClickListener
            }
        }
    }
}
