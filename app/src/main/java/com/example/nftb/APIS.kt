package com.example.nftb


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS {
    @POST("/KotlinTest")
    //@Headers("accept : application/json","content-type : application/json" )
    fun post_users(
        @Body jsonparams:PostModel
    ) : Call<PostResult>

    @POST("/GeoCoding")
    //@Headers("accept : application/json","content-type : application/json" )
    fun post_coord(
        @Body jsonparams:Coordinate
    ) : Call<PostResult_2>

    companion object {
        private const val  BASE_URL = "https://ce-l.kr/"

        fun create() : APIS {

            val gson :Gson = GsonBuilder().setLenient().create();

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}