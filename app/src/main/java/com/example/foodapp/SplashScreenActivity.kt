package com.example.foodapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.foodapp.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        Thread.sleep(1000)
        screenSplash.setKeepOnScreenCondition { false }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

//        val content: View = findViewById(android.R.id.content)
//        content.viewTreeObserver.addOnPreDrawListener(
//            object: ViewTreeObserver.OnPreDrawListener{
//                override fun onPreDraw(): Boolean {
//                    // Check if the initial data is ready.
//                    return if (viewModel.isReady){
//                        //The content is ready; start drawing.
//                        content.viewTreeObserver.removeOnPreDrawListener(this)
//                        true
//                    } else {
//                        //The content is not ready; suspend.
//                        false
//                    }
//                }
//            }
//        )
    }
}