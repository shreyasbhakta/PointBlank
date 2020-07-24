package com.dscepointblank.pointblank.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dscepointblank.pointblank.R
import com.dscepointblank.pointblank.activities.TOPIC
import com.dscepointblank.pointblank.models.UpdateModel
import com.dscepointblank.pointblank.notifications.NotificationData
import com.dscepointblank.pointblank.notifications.PushNotification
import com.dscepointblank.pointblank.utilityClasses.DownloadController
import com.dscepointblank.pointblank.utilityClasses.RetrofitInstance
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home_screen.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeScreenFragment : Fragment() {

    lateinit var downloadController: DownloadController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home_screen, container, false)

        view.btn_sendNotificationHomeScreen.setOnClickListener {
            if (view.tv_notificationTitleHomeScreen.text.toString().isNotEmpty() && view.tv_notificationDesHomeScreen.text.toString().isNotEmpty()) {

                PushNotification(
                    NotificationData(
                        view.tv_notificationTitleHomeScreen.text.toString(),
                        view.tv_notificationDesHomeScreen.text.toString()
                    ), TOPIC
                )
                    .also { sendNotification(it) }
            }
        }


        view.btn_updateAppHomeScreen.setOnClickListener {
            checkForUpdates()
        }
        return  view
    }

    private fun sendNotification(notification: PushNotification) =
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d("DDDD", "Response is ${Gson().toJson(response.toString())}")
                }

            } catch (e: Exception) {
                Log.d("DDDD", e.localizedMessage!!)
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       downloadController.onPermissionResult(requestCode,permissions,grantResults)
    }

    private fun checkForUpdates() =
        try {
            downloadController =
                DownloadController(
                    requireContext(),
                    UpdateModel(1, "d")
                )
            downloadController.beginDownloadProcess()
        }
        catch (e :Exception)
        {
            Log.d("DDDD",e.localizedMessage!!)
        }
}