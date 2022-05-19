package com.wael.tyvolunty

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.activity_detail_call.description
import kotlinx.android.synthetic.main.activity_detail_call.restodetail_img
import kotlinx.android.synthetic.main.activity_detail_call.restonamedetail
import kotlinx.android.synthetic.main.activity_detail_call.retour
import kotlinx.android.synthetic.main.activity_detail_call_recruiter.*


class DetailCallRecruiter : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_call_recruiter)
        restonamedetail.setText(intent.getStringExtra("name").toString())
        val id = intent.getStringExtra("id").toString()
        description.setText(intent.getStringExtra("description").toString())
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        val filename = intent.getStringExtra("picture").toString()

        Log.e("filename====",filename)
        if (filename == "null"){
            Log.e("here====","heeree")
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F2022_04_27_01_01_21?alt=media"
            Glide.with(this)
                .load(path)
                .into(restodetail_img)
        }else{
            Log.e("here====","yooooo")
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F$filename?alt=media&token=73554f53-d1cd-4839-8543-f555c21406f1"
            Glide.with(this)
                .load(path)
                .into(restodetail_img)
        }
        retour.setOnClickListener {
            finish()
        }
        declined.setText(intent.getStringExtra("declined").toString())
        accepted.setText(intent.getStringExtra("accepted").toString())
        pending.setText(intent.getStringExtra("pending").toString())


        //lisnter
        reserve.setOnClickListener {
            val intent = Intent(this, pendinglist::class.java)


            intent.putExtra("id", id)

            startActivity(intent)
            finish()
        }
    }

}