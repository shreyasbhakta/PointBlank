package com.dscepointblank.pointblank.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dscepointblank.pointblank.R
import com.dscepointblank.pointblank.notifications.MyNotifications
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  notifications = MyNotifications(this)

        button.setOnClickListener {
            notifications.createNotification("Hey User!!","Point Blank Welcomes you")
        }
    }
}
