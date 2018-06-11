package com.bapercoding.mycloud

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bapercoding.mycloud.Adapter.RVAdapterMain
import com.bapercoding.mycloud.RetrofitConfig.ApiConfig
import com.bapercoding.mycloud.RetrofitConfig.Response.Default
import com.bapercoding.mycloud.RetrofitConfig.Response.MyFileList
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var loading:ProgressDialog
    lateinit var rvAdapterMain: RVAdapterMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "My Cloud"

        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.setHasFixedSize(true)
        loading = ProgressDialog(this)

        fabUploadFiles.setOnClickListener(this)
        PRDownloader.initialize(this)

    }

    override fun onClick(p0: View?) {

        when(p0){

            fabUploadFiles->{
                startActivity(Intent(this,UploadActivity::class.java))
            }

        }

    }

    override fun onResume() {
        super.onResume()
        loadList()
    }

    private fun loadList(){

        loading.setMessage("Mohon tunggu...")
        loading.show()

        val call = ApiConfig().getInstance().myFileList()
        call.enqueue(object : retrofit2.Callback<MyFileList>{

            override fun onFailure(call: Call<MyFileList>?, t: Throwable?) {
                loading.dismiss()
                Toast.makeText(applicationContext,"Connection Failure",Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
            }

            override fun onResponse(call: Call<MyFileList>?, response: Response<MyFileList>?) {

                if(response != null){

                    if(response.isSuccessful){

                        loading.dismiss()

                        if(response.body()?.allfiles?.size == 0){
                            lbDataEmpty.visibility = View.VISIBLE
                        }

                        rvAdapterMain = RVAdapterMain(this@MainActivity,response.body()?.allfiles)
                        rvMain.adapter = rvAdapterMain


                    }

                }

            }


        })

    }

    fun downloadFiles(file:String?){

        if(EasyPermissions.hasPermissions(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            val downloadedFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            Log.d("ABSOLUTEPATH",downloadedFile.absolutePath)

            if(!downloadedFile.exists()){
                downloadedFile.mkdir()
            }

            loading.setMessage("Downloading your files...")
            loading.setCancelable(false)
            loading.show()

            PRDownloader.download(ApiConfig.filesUrl+file, downloadedFile.absolutePath, file)
                    .build()
                    .start(object : OnDownloadListener{

                        override fun onDownloadComplete() {

                            loading.dismiss()
                            Toast.makeText(applicationContext,"Download Successfull, Check your download folder",Toast.LENGTH_LONG).show()

                        }

                        override fun onError(error: Error?) {
                            loading.dismiss()
                            Toast.makeText(applicationContext,"Download Error",Toast.LENGTH_SHORT).show()
                            Log.d("DOWNLOADERROR",error.toString())
                        }


                    })

        }else{
            EasyPermissions.requestPermissions(this,"Application need your permission for accessing the Storage",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            EasyPermissions.requestPermissions(this,"Application need your permission for accessing the Storage",992,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }



    }

    fun deleteFiles(id:String?){

        loading.setMessage("Mohon tunggu...")
        loading.show()

        val call = ApiConfig().getInstance().delete(id)
        call.enqueue(object : retrofit2.Callback<Default>{

            override fun onFailure(call: Call<Default>?, t: Throwable?) {
                loading.dismiss()
                Toast.makeText(applicationContext,"Connection Failure",Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
            }

            override fun onResponse(call: Call<Default>?, response: Response<Default>?) {


                if(response != null){

                    if(response.isSuccessful){

                        loading.dismiss()
                        var message = response.body()?.message
                        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

                        if(message!!.contains("successfull")){
                            this@MainActivity.onResume()
                        }


                    }

                }

            }


        })
    }

}
