package com.bapercoding.mycloud.RetrofitConfig.Model

import com.google.gson.annotations.SerializedName

class Files {

    @SerializedName("id")
    var id:String? = null

    @SerializedName("filename")
    var filename:String? = null

    @SerializedName("extension")
    var extension:String? = null

    @SerializedName("file")
    var file:String? = null

}