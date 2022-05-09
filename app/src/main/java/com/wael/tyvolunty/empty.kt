package com.wael.tyvolunty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class empty : AppCompatActivity() {
    private lateinit var txtname: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
        txtname = findViewById(R.id.test)
    }
}