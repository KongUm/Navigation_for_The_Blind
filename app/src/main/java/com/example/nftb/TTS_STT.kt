package com.example.nftb

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import java.util.*

private var tts: TextToSpeech? = null

lateinit var recognitionListener: RecognitionListener

//TTS 관련 함수

fun initTextToSpeech(context: Context) {
    tts = TextToSpeech(context) {
        if (it == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT)
                    .show()
                return@TextToSpeech
            }
            Toast.makeText(context, "TTS setting successed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "TTS init failed", Toast.LENGTH_SHORT).show()

            //TODO : 토스트 메세지 삭제
        }
    }
}

fun ttsSpeak(strTTS: String) {
    tts?.speak(strTTS, TextToSpeech.QUEUE_ADD, null, null)
}


//아래부터 STT관련 함수

fun requestPermission(context: Context, activity : Activity) {
    if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context,
            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

        // 거부해도 계속 노출됨. ("다시 묻지 않음" 체크 시 노출 안됨.)
        // 허용은 한 번만 승인되면 그 다음부터 자동으로 허용됨.
        ActivityCompat.requestPermissions(activity,
            arrayOf(Manifest.permission.RECORD_AUDIO), 0)
    }
}

fun setListener(context: Context) {
    recognitionListener = object: RecognitionListener {

        override fun onReadyForSpeech(params: Bundle?) {
            Toast.makeText(context, "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show()
        }

        override fun onBeginningOfSpeech() {

        }

        override fun onRmsChanged(rmsdB: Float) {

        }

        override fun onBufferReceived(buffer: ByteArray?) {

        }

        override fun onEndOfSpeech() {

        }

        override fun onError(error: Int) {
            val message: String

            when (error) {
                SpeechRecognizer.ERROR_AUDIO ->
                    message = "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT ->
                    message = "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                    message = "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK ->
                    message = "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                    message = "네트워크 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH ->
                    message = "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                    message = "RECOGNIZER가 바쁨"
                SpeechRecognizer.ERROR_SERVER ->
                    message = "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                    message = "말하는 시간초과"
                else ->
                    message = "알 수 없는 오류"
            }
            Toast.makeText(context, "에러 발생 $message", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(results: Bundle?) {
            val matches: ArrayList<String> = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>

            for (i in 0 until matches.size) {
                start_address = matches[i]
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {

        }

        override fun onEvent(eventType: Int, params: Bundle?) {

        }
    }
}


