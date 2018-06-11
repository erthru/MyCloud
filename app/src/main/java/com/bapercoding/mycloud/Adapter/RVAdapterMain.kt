package com.bapercoding.mycloud.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bapercoding.mycloud.MainActivity
import com.bapercoding.mycloud.R
import com.bapercoding.mycloud.RetrofitConfig.Model.Files
import kotlinx.android.synthetic.main.list_main.view.*
import pub.devrel.easypermissions.EasyPermissions

class RVAdapterMain(context: MainActivity, arrayList: ArrayList<Files>?) : RecyclerView.Adapter<RVAdapterMain.Holder>() {

    val context = context
    val arrayList = arrayList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_main,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.lbFileNameList.text = "File Name : "+arrayList?.get(position)?.filename
        holder.lbExtensionList.text = "Extension : "+arrayList?.get(position)?.extension

        holder.lbDownloadList.setOnClickListener {

            AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Download this file ?")
                    .setPositiveButton("DOWNLOAD",DialogInterface.OnClickListener { dialogInterface, i ->
                        context.downloadFiles(arrayList?.get(position)?.file)
                    })
                    .setNegativeButton("CANCEL",DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .show()

        }

        holder.lbDeleteList.setOnClickListener {

            AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Delete this file ?")
                    .setPositiveButton("DELETE",DialogInterface.OnClickListener { dialogInterface, i ->
                        context.deleteFiles(arrayList?.get(position)?.id)
                    })
                    .setNegativeButton("CANCEL",DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .show()

        }

    }


    class Holder(view:View) : RecyclerView.ViewHolder(view){

        val lbFileNameList:TextView = view.findViewById(R.id.lbFileNameList)
        val lbExtensionList:TextView = view.findViewById(R.id.lbExtensionList)
        val lbDownloadList:TextView = view.findViewById(R.id.lbDownloadList)
        val lbDeleteList:TextView = view.findViewById(R.id.lbDeleteList)

    }

}