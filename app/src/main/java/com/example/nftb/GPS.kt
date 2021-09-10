package com.example.nftb

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

var start_latitude = ""
var start_longitude = ""

var current_longitude = ""
var current_latitude = ""

fun GpsTracker(context : Context, activity : Activity) {

    val lm = activity.getSystemService(LOCATION_SERVICE) as LocationManager

    val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    val isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    //매니페스트에 권한이 추가되어 있다해도 여기서 다시 한번 확인해야함
    if (Build.VERSION.SDK_INT >= 23 &&
        ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            0
        )
    } else {
        when { //프로바이더 제공자 활성화 여부 체크
            isNetworkEnabled -> {
                val location =
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                start_longitude = location?.longitude.toString()
                start_latitude = location?.latitude.toString()
                Toast.makeText(context, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
            }
            isGPSEnabled -> {
                val location =
                    lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                start_longitude = location?.longitude.toString()
                start_latitude = location?.latitude.toString()
                Toast.makeText(context, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            500, //몇초
            1F,   //몇미터
            gpsLocationListener
        )
        lm.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            500,
            1F,
            gpsLocationListener
        )

        //해제부분. 상황에 맞게 잘 구현하자
        lm.removeUpdates(gpsLocationListener)
    }
}

//위에 *몇초 간격과 몇미터를 이동했을시에 호출되는 부분* 에 필요한 정보
//주기적으로 위치 업데이트 안할거면 사용하지 않음
val gpsLocationListener = object : LocationListener {
    override fun onLocationChanged(location: Location) {
       // val provider: String = location.provider
       // val altitude: Double = location.altitude
        current_longitude = location.longitude.toString()
        current_latitude = location.latitude.toString()

        Log.d("debug", current_latitude.toString())
        Log.d("debug", current_longitude.toString())
    }

    //아래 3개함수는 형식상 필수부분
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}