package com.dscepointblank.pointblank.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dscepointblank.pointblank.R
import com.dscepointblank.pointblank.notifications.*
import com.dscepointblank.pointblank.utilityClasses.RetrofitInstance
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

const val TOPIC = "/topics/MyTopic"

class MainActivity : BaseActivity() {
    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
    }
    lateinit var downloadController: DownloadController
    var firebaseRemoteConfig: FirebaseRemoteConfig? = null
    var version=1.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        button.setOnClickListener {
            if (notititle.text.toString().isNotEmpty() && notides.text.toString().isNotEmpty()) {

                PushNotification(
                    NotificationData(
                        notititle.text.toString(),
                        notides.text.toString()
                    ), TOPIC
                )
                    .also { sendNotification(it) }
            }
        }

        codeForces.setOnClickListener {
            if(codeIdTV.text.toString().isNotEmpty())
                getCodeForces(codeIdTV.text.toString())
        }

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder().build()

        firebaseRemoteConfig!!.setConfigSettings(configSettings)
        val defaultValue = HashMap<String, Any>()

        defaultValue["ver"] = 1.0
        defaultValue["apk_url"] = "app_apk"

        firebaseRemoteConfig!!.setDefaults(defaultValue)
        update.setOnClickListener {
            firebaseRemoteConfig!!.fetch(0)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        firebaseRemoteConfig!!.activateFetched()

                        if (version != firebaseRemoteConfig!!.getDouble("ver")) {

                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("Update Required")
                            builder.setMessage("You must update the app to continue further")
                            builder.setIcon(android.R.drawable.ic_dialog_alert)

                            builder.setPositiveButton("Update") { dialogInterface, which ->
                                val apkUrl = firebaseRemoteConfig!!.getString("apk_url")
                                downloadController = DownloadController(this, apkUrl)
                                checkStoragePermission()
                            }
                            builder.setNegativeButton("Cancel"){dialogInterface, which ->}

                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(true)
                            alertDialog.show()
                        }
                        else{

                            val builder = AlertDialog.Builder(this)

                            builder.setTitle("App is up to date")
                            builder.setMessage("You are using the latest version of the app.")
                            builder.setNegativeButton("Ok"){dialogInterface, which ->}

                            val alertDialog: AlertDialog = builder.create()
                            alertDialog.setCancelable(true)
                            alertDialog.show()
                        }

                    }

                }
        }



    }

    private fun getCodeForces(userId: String) = GlobalScope.launch(Dispatchers.IO) {
        try {
            val userData = RetrofitInstance.codeForcesAPI.getUserDetails(userId)
            withContext(Dispatchers.Main)
            {
                Toast.makeText(this@MainActivity, userData.details[0].firstName, Toast.LENGTH_SHORT)
                    .show();
            }

        } catch (e: Exception) {
            Log.d("NNNN", "error" + e.localizedMessage!!)
        }
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


    /*  ***********************************
         (Begin) Functions for App Update
        *********************************** */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadController.enqueueDownload()
            } else {
                mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun checkStoragePermission() {
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            downloadController.enqueueDownload()
        } else {
            requestStoragePermission()
        }
    }
    private fun requestStoragePermission() {
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mainLayout.showSnackbar(
                R.string.storage_access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )
            }
        } else {
            requestPermissionsCompat(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_STORAGE
            )
        }
    }
    fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission)
    fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
    fun AppCompatActivity.requestPermissionsCompat(
        permissionsArray: Array<String>,
        requestCode: Int
    ) {
        ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
    }


    fun View.showSnackbar(msgId: Int, length: Int) {
        showSnackbar(context.getString(msgId), length)
    }
    fun View.showSnackbar(msg: String, length: Int) {
        showSnackbar(msg, length, null, {})
    }
    fun View.showSnackbar(
        msgId: Int,
        length: Int,
        actionMessageId: Int,
        action: (View) -> Unit
    ) {
        showSnackbar(context.getString(msgId), length, context.getString(actionMessageId), action)
    }
    fun View.showSnackbar(
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(this, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        }
    }
    /*  ***********************************
        (End) Functions for App Update
        *********************************** */

}

