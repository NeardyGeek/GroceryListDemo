package com.nerdygeek.grocerylistdemo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.nerdygeek.grocerylistdemo.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
    }

    private fun init() {
        Handler().postDelayed({
            loadActivity()
        }, 3000)
    }

    private fun loadActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}