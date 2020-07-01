package com.dscepointblank.pointblank.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dscepointblank.pointblank.R
import com.dscepointblank.pointblank.utilityClasses.MyNotifications
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener { val notify = MyNotifications(this)
        notify.createNotification()}
    }
}
