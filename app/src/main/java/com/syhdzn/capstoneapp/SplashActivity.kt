 package com.syhdzn.capstoneapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowInsets
import android.view.WindowManager
import com.syhdzn.capstoneapp.databinding.ActivitySplashBinding
import com.syhdzn.capstoneapp.databinding.ActivityWelcomeBinding
import com.syhdzn.capstoneapp.ui.welcome.WelcomeActivity

 class SplashActivity : AppCompatActivity() {

     private lateinit var binding: ActivitySplashBinding
     private val splashTimeOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        Handler().postDelayed({
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }, splashTimeOut)
    }

     private fun setupView() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
             window.insetsController?.hide(WindowInsets.Type.statusBars())
         } else {
             window.setFlags(
                 WindowManager.LayoutParams.FLAG_FULLSCREEN,
                 WindowManager.LayoutParams.FLAG_FULLSCREEN
             )
         }
         supportActionBar?.hide()
     }

}