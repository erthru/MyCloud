package com.bapercoding.mycloud.RetrofitConfig.Response

import com.bapercoding.mycloud.RetrofitConfig.Model.Files
import com.google.gson.annotations.SerializedName

class MyFileList {

    @SerializedName("error")
    var error:String? = null

    @SerializedName("allfiles")
    var allfiles:ArrayList<Files>? = null

}