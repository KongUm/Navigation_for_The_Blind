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
import android.speech.tts.TextToSpeech
import android.system.Os.rename
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
import java.util.*
import kotlin.concurrent.timer
import kotlin.math.log

var current_longitude: Double = 0.0
var current_latitude: Double = 0.0

class Navigation_3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation3)
        initTextToSpeech()
        val TvMove = findViewById<TextView>(R.id.tv_move)
        TvMove.setText("목적지 : $Destination_info")

        val binding by lazy { ActivityNavigation1Binding.inflate(layoutInflater) }
        val api = APIS.create()

        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val BtnNaviStart = findViewById<Button>(R.id.btn_navistart)
        val TvResult = findViewById<TextView>(R.id.TV_Result)

        BtnNaviStart.setOnClickListener {

            //TODO: MainActivity, Navigation 1,2 종료

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
                        TvMove.setText("이동중: $Destination_info")
                        TvResult.setText("$getLatitude, $getLongitude")
                    }
                    isGPSEnabled -> {
                        val location =
                            lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                        var getLongitude = location?.longitude!!
                        var getLatitude = location.latitude
                        Toast.makeText(this, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
                        TvMove.setText("이동중: $Destination_info")
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
        var ResponseBodyResult :String? = ""
        val data = PostModel("127.055904", "37.5169814", "127.058796", "37.5125020")
        Log.d("Sendlog", "$current_longitude, $current_latitude")
        timer(period = 20000){
            api.post_users(data).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())



                    if(response.body()?.result != null){
                        if(response.body()?.result.toString().replace(" ", "").replace("//d".toRegex(), "")
                            != ResponseBodyResult?.replace(" ", "")?.replace("//d".toRegex(), ""))
                        {
                            Log.d("Iflog", "1st ${response.body()?.result.toString().replace(" ", "").replace("//d".toRegex(), "")}")
                            Log.d("Iflog", "2nd ${ResponseBodyResult?.replace(" ", "")?.replace("//d".toRegex(), "")}")
                            ResponseBodyResult = response.body()?.result

                            ttsSpeak("${response.body()?.result}")
                        }else{Log.d("IFlog", "성공")}
                    }else {
                        Log.d("ERROR", "ERROR - null!!")
                    }
                }

                override fun onFailure(call: Call<PostResult>, t: Throwable) {
                    binding.tvCurrloca.setText("주소 변환에 실패하였습니다.")
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                }
            })
        }

        lm.removeUpdates(gpsLocationListener)
    }

    //위에 *몇초 간격과 몇미터를 이동했을시에 호출되는 부분* 에 필요한 정보
    //주기적으로 위치 업데이트 안할거면 사용하지 않음
    val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            current_longitude = location.longitude
            Log.d("loca", current_longitude.toString())
            current_latitude = location.latitude
            val altitude: Double = location.altitude


            val TvResult2 = findViewById<TextView>(R.id.TV_Result2)
            TvResult2.setText("$current_latitude, $current_longitude 히히")

            Log.d("debug", current_latitude.toString())
            Log.d("debug", current_longitude.toString())
        }

        //아래 3개함수는 형식상 필수부분
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private var tts: TextToSpeech? = null

    private fun initTextToSpeech() {
        tts = TextToSpeech(this) {
            if (it == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.KOREAN)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT)
                        .show()
                    return@TextToSpeech
                }
                Toast.makeText(this, "TTS setting successed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "TTS init failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun ttsSpeak(strTTS: String) {
        tts?.speak(strTTS, TextToSpeech.QUEUE_ADD, null, null)
    }
}