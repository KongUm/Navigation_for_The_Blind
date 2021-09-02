package com.example.nftb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.timer

private var gpsTracker: GpsTracker? = null

class Navigation_3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation3)

        val Tvdest = findViewById<TextView>(R.id.tv_dest)
        Tvdest.setText("목적지\n$Destination_info")

        Gps_realtime()
    }

    fun Gps_realtime() {

        val Tvt2 = findViewById<TextView>(R.id.tv_t2)
        val BtnNaviStart = findViewById<Button>(R.id.btn_navistart)

        BtnNaviStart.setOnClickListener() {
            gpsTracker = GpsTracker(this@Navigation_3)
            var cur_latitude = gpsTracker!!.getLatitude()
            val cur_longitude = gpsTracker!!.getLongitude()

            Tvt2.setText("$cur_latitude, $cur_longitude")

            //TODO : 안내시작 버튼 클릭시 Main,1,2 Activity 종료
        }
    }
}