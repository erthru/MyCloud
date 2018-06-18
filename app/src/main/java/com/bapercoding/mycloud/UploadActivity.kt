package com.bapercoding.mycloud

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_upload.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.bapercoding.mycloud.RetrofitConfig.ApiConfig
import com.bapercoding.mycloud.RetrofitConfig.Response.Default
import com.downloader.PRDownloader
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.regex.Pattern


class UploadActivity : AppCompatActivity(),View.OnClickListener {

    lateinit var file:MultipartBody.Part

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        supportActionBar?.title = "Upload File"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnUpload.setOnClickListener(this)
        btnSelectFile.setOnClickListener(this)

    }


    override fun onClick(p0: View?) {

        when(p0){

            btnSelectFile->{
                if(txFilename.text.isNullOrBlank() || txExtension.text.isNullOrBlank()){
                    Toast.makeText(applicationContext,"Fill the required field first",Toast.LENGTH_SHORT).show()
                }else{

                    if(EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                        MaterialFilePicker()
                                .withActivity(this)
                                .withRequestCode(1)
                                .withHiddenFiles(true)
                                .start()
                    }else{
                        EasyPermissions.requestPermissions(this,"Application need your permission for accessing the Storage",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        EasyPermissions.requestPermissions(this,"Application need your permission for accessing the Storage",992,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }


                }
            }

            btnUpload->{
                upload()
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            android.R.id.home->{
                this.finish()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){

            txFilename.visibility = View.GONE
            txExtension.visibility = View.GONE
            btnSelectFile.visibility = View.GONE
            lbFile.visibility = View.VISIBLE
            lbNewFilename.visibility = View.VISIBLE
            btnUpload.visibility = View.VISIBLE

            lbFile.text = "File to upload : "+data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            lbNewFilename.text = "New File Name : "+txFilename.text.toString()+"."+txExtension.text.toString()

            val fileToUpload = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),File(fileToUpload))
            file = MultipartBody.Part.createFormData("file",File(fileToUpload)?.name,requestBody)

        }

    }




    private fun upload(){

        val loading = ProgressDialog(this)
        loading.setMessage("Uploading file...")
        loading.show()

        var requestBodyFilename = RequestBody.create(MediaType.parse("multipart/form-data"),txFilename.text.toString())
        var requestBodyExtension = RequestBody.create(MediaType.parse("multipart/form-data"),txExtension.text.toString())
        val call = ApiConfig().getInstance().upload(requestBodyFilename,requestBodyExtension,file)
        call.enqueue(object : Callback<Default>{

            override fun onFailure(call: Call<Default>?, t: Throwable?) {
                loading.dismiss()
                Toast.makeText(applicationContext,"CONNECTION FAILURE",Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
            }

            override fun onResponse(call: Call<Default>?, response: Response<Default>?) {

                if(response != null){
                    if(response.isSuccessful){

                        loading.dismiss()

                        var message = response?.body()?.message

                        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

                        if(message!!.contains("successfull")){
                            finish()
                        }

                        if(message!!.contains("already exist")){
                            recreate()
                        }

                    }
                }

            }


        })

    }

}
