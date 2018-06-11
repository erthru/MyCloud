package com.bapercoding.mycloud.RetrofitConfig

import com.bapercoding.mycloud.RetrofitConfig.Response.Default
import com.bapercoding.mycloud.RetrofitConfig.Response.MyFileList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ApiConfig {

    companion object {
        val baseUrl = "http://192.168.43.39/anows/mycloud/"
        val filesUrl = "http://192.168.43.39/anows/mycloud/files/"
    }

    fun getRetrofit() : Retrofit{

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }

    fun getInstance() : ApiInterface{

        return getRetrofit().create(ApiInterface::class.java)

    }

}

interface ApiInterface{

    @GET("myfilelist.php")
    fun myFileList() : Call<MyFileList>

    @Multipart
    @POST("upload.php")
    fun upload(
            @Part("filename") filename:RequestBody?,
            @Part("extension") extension:RequestBody?,
            @Part file:MultipartBody.Part
    ) : Call<Default>

    @GET("delete.php")
    fun delete(
            @Query("id") id:String?
    ) : Call<Default>

}