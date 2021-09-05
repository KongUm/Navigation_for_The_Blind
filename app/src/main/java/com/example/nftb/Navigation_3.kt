package com.example.nftb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.nftb.databinding.ActivityNavigation1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

var current_longitude = 0.0
var current_latitude = 0.0

class Navigation_3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation3)

        val binding by lazy { ActivityNavigation1Binding.inflate(layoutInflater) }
        val api = APIS.create()

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val BtnNaviStart = findViewById<Button>(R.id.btn_navistart)
        val TvResult = findViewById<TextView>(R.id.TV_Result)

        BtnNaviStart.setOnClickListener {
            val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            //매니페스트에 권한이 추가되어 있다해도 여기서 다시 한번 확인해야함
            if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@Navigation_3,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
            } else {
                when { //프로바이더 제공자 활성화 여부 체크
                    isNetworkEnabled -> {
                        val location =
                            lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                        var getLongitude = location?.longitude!!
                        var getLatitude = location.latitude
                        Toast.makeText(this, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
                        TvResult.setText("$getLatitude, $getLongitude")
                    }
                    isGPSEnabled -> {
                        val location =
                            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                        var getLongitude = location?.longitude!!
                        var getLatitude = location.latitude
                        Toast.makeText(this, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
                        TvResult.setText("$getLatitude, $getLongitude")
                    }
                    else -> {
                    }
                }
                lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, //몇초
                    1F,   //몇미터
                    gpsLocationListener
                )
                lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    1F,
                    gpsLocationListener
                )

                //해제부분. 상황에 맞게 잘 구현하자
                //lm.removeUpdates(gpsLocationListener)
            }
        }
        val data = PostModel("$current_longitude", "$current_latitude", "$Dest_long", "$Dest_lat")
        Log.d("debug", current_latitude.toString())

        api.post_users(data).enqueue(object : Callback<PostResult> {
            override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())

                if(response.body()?.result == "errorr_SSiBal"){
                    Log.d("SS", "sibal")
                }else {}
            }

            override fun onFailure(call: Call<PostResult>, t: Throwable) {
                binding.tvCurrloca.setText("주소 변환에 실패하였습니다.")
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }
        })
        lm.removeUpdates(gpsLocationListener)
    }

    //위에 *몇초 간격과 몇미터를 이동했을시에 호출되는 부분* 에 필요한 정보
    //주기적으로 위치 업데이트 안할거면 사용하지 않음
    val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            val longitude: Double = location.longitude
            val latitude: Double = location.latitude
            val altitude: Double = location.altitude

            val TvResult2 = findViewById<TextView>(R.id.TV_Result2)
            TvResult2.setText("$latitude, $longitude 히히")

            Log.d("debug", latitude.toString())
            Log.d("debug", longitude.toString())
        }

        //아래 3개함수는 형식상 필수부분
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
}