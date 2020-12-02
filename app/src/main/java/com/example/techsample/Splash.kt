package com.example.techsample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 2000L
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preferences=getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        if(preferences.getString("login", "no").equals("yes"))
        {
            val intent=Intent(this,MainActivity ::class.java)
            startActivity(intent)
        }
        else{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }


        Handler().postDelayed(
                {
                    val i = Intent(this@Splash, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }, SPLASH_TIME_OUT
        )

    }
}