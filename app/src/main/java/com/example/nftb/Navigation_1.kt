package com.example.nftb

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.nftb.databinding.ActivityNavigation1Binding

private lateinit var speechRecognizer: SpeechRecognizer
var start_address : String? = null

class Navigation_1 : AppCompatActivity() {

    val binding by lazy { ActivityNavigation1Binding.inflate(layoutInflater) }
    val api = APIS.create()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = Coordinate("$start_longitude", "$start_latitude")
        api.post_coord(data).enqueue(object : Callback<PostResult_2> {

            override fun onResponse(call: Call<PostResult_2>, response: Response<PostResult_2>) {
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())

                if(response.body()?.name == null){
                    binding.tvCurrloca.setText("주소 변환에 실패하였습니다. null")
                }else {binding.tvCurrloca.setText("현재 위치 : ${response.body()?.name.toString()}")}
            }

            override fun onFailure(call: Call<PostResult_2>, t: Throwable) {
                binding.tvCurrloca.setText("주소 변환에 실패하였습니다.")
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }
        })

        requestPermission(this, this)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")

        binding.btnSttstart.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(intent)
            setListener(this)

            Log.d("log", "버튼 클릭")
        }
        if(start_address != null){
            for (i in Dest_name.indices) {
                if(Dest_name[i].replace(" ","") == start_address?.replace(" ","") || Dest_address[i].replace(" ","") == start_address?.replace(" ","")){
                }
                val intent = Intent(this, Navigation_2::class.java)
                startActivity(intent)
            }
        }
    }
}




