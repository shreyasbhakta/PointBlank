package com.dscepointblank.pointblank.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dscepointblank.pointblank.R
import com.dscepointblank.pointblank.fragments.HomeScreenFragment
import com.dscepointblank.pointblank.fragments.WebViewFrag
import com.dscepointblank.pointblank.models.UpdateModel
import com.dscepointblank.pointblank.notifications.PushNotification
import com.dscepointblank.pointblank.utilityClasses.DownloadController
import com.dscepointblank.pointblank.utilityClasses.RetrofitInstance
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TOPIC = "/topics/MyTopic"

class MainActivity : BaseActivity() {
    private val forumWebView: Fragment = WebViewFrag.newInstance("https://forum.dsce.in/")
    private val writeupWebView: Fragment = WebViewFrag.newInstance("https://writeups.dsce.in/")
    private val homefrag: Fragment =HomeScreenFragment()
    private val fm = supportFragmentManager

    private var visibleWebView: Fragment = forumWebView

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menuForm->{
                fab.setImageResource(R.drawable.home)
                fm.beginTransaction().hide(visibleWebView).show(forumWebView).commit()
                visibleWebView = forumWebView
                fab.setOnClickListener {
                    fab.setImageResource(R.drawable.ic_baseline_add_24)
                    fm.beginTransaction().hide(visibleWebView).show(homefrag).commit()
                    visibleWebView = homefrag
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.menuWrite -> {
                fab.setImageResource(R.drawable.home)
                fm.beginTransaction().hide(visibleWebView).show(writeupWebView).commit()
                visibleWebView = writeupWebView

                fab.setOnClickListener {
                    fab.setImageResource(R.drawable.ic_baseline_add_24)
                    fm.beginTransaction().hide(visibleWebView).show(homefrag).commit()
                    visibleWebView = homefrag
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    lateinit var downloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fm.beginTransaction().add(R.id.fragment2, writeupWebView).hide(writeupWebView).commit()
        fm.beginTransaction().add(R.id.fragment2, forumWebView).hide(forumWebView).commit()

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)



//        fab.setBackgroundResource(R.drawable.box)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

//
//        button.setOnClickListener {
//            if (notititle.text.toString().isNotEmpty() && notides.text.toString().isNotEmpty()) {
//
//                PushNotification(
//                    NotificationData(
//                        notititle.text.toString(),
//                        notides.text.toString()
//                    ), TOPIC
//                )
//                    .also { sendNotification(it) }
//            }
//        }
//
//        codeForces.setOnClickListener {
//            if(codeIdTV.text.toString().isNotEmpty())
//                getCodeForces(codeIdTV.text.toString())
//        }
//
//        update.setOnClickListener { checkForUpdates() }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent = Intent(visibleWebView.hashCode().toString())
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            true
        } else
            super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        val navHostFragment =
            supportFragmentManager.primaryNavigationFragment
        val fragment =
            navHostFragment!!.childFragmentManager.fragments[0]


        (fragment as HomeScreenFragment).onRequestPermissionsResult(requestCode,permissions,grantResults)
super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun checkForUpdates() =
        try {
            downloadController =
                DownloadController(
                    this@MainActivity,
                    UpdateModel(1, "d")
                )
            downloadController.beginDownloadProcess()
        }
        catch (e :Exception)
        {
            Log.d("DDDD",e.localizedMessage!!)
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
}

