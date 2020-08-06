package com.dscepointblank.pointblank.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dscepointblank.pointblank.R

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
