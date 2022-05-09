package com.wael.tyvolunty

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView


class splashscreen : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)
       val image : ImageView =findViewById(R.id.imageViewsplash)
        val anim = AnimationUtils.loadAnimation(this,R.anim.splashanimation)
        image.startAnimation(anim)
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        if (mSharedPref.getString(USERNAME,null) !=null){
            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this, Accueil::class.java)
                startActivity(mainIntent)
                finish()
            }, 3000)
        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this, choose::class.java)
                startActivity(mainIntent)
                finish()
            }, 3000)
        }
        /**/
    }
}