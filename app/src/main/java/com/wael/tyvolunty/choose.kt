package com.wael.tyvolunty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choose.*

class choose : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)
        recruiter.setOnClickListener {
            val mainIntent = Intent(this, SigninRecruiter::class.java)
            startActivity(mainIntent)
            finish()
        }
        volunteer.setOnClickListener {
            val mainIntent = Intent(this, Login::class.java)
            startActivity(mainIntent)
            finish()
        }
    }
}