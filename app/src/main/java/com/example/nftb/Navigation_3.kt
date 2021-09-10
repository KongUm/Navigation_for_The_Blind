package com.example.nftb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.nftb.databinding.ActivityNavigation1Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer



class Navigation_3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation3)

        val binding by lazy { ActivityNavigation1Binding.inflate(layoutInflater) }
        val BtnNaviStart = findViewById<Button>(R.id.btn_navistart)
        val TvResult = findViewById<TextView>(R.id.TV_Result)
        val TvMove = findViewById<TextView>(R.id.tv_move)
        val api = APIS.create()
        var ResponseBodyResult :String? = ""

        initTextToSpeech(this)
        TvMove.setText("목적지 : $Destination_info")

        val data = PostModel(current_longitude, current_latitude, Destination_long, Destination_lat)
        Log.d("Sendlog", "$current_longitude, $current_latitude")
        timer(period = 10000){
            api.post_users(data).enqueue(object : Callback<PostResult> {
                override fun onResponse(call: Call<PostResult>, response: Response<PostResult>) {
                    Log.d("log", response.toString())
                    Log.d("log", response.body().toString())

                    if(response.body()?.result != null){
                        if(response.body()?.result.toString().replace(" ", "").replace("//d".toRegex(), "")
                            != ResponseBodyResult?.replace(" ", "")?.replace("//d".toRegex(), ""))
                        {
                            ResponseBodyResult = response.body()?.result

                            ttsSpeak("${response.body()?.result}")
                        }else{Log.d("Iflog", "성공")}
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
    }


}

