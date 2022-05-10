package com.wael.tyvolunty

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.wael.tyvolunty.adapters.CallsAdapterRecruiter
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_calls.*
import kotlinx.android.synthetic.main.activity_calls.progBar
import kotlinx.android.synthetic.main.activity_calls.rv_calls
import kotlinx.android.synthetic.main.activity_calls_recruiter.*
import kotlinx.android.synthetic.main.item_bulle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CallsRecruiter : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calls_recruiter)
        progBar.visibility = View.VISIBLE
        fab.visibility = View.GONE
        mSharedPref = getSharedPreferences(PREF_NAMER, MODE_PRIVATE);
        linearLayoutManager = LinearLayoutManager(this)
        rv_callsR.layoutManager = linearLayoutManager
        mSharedPref = getSharedPreferences(PREF_NAMER, MODE_PRIVATE);
        fab.setOnClickListener {
            val mainIntent = Intent(this, AddCall::class.java)
            startActivity(mainIntent)
        }
        if(mSharedPref.getString(PHOTOR, "").toString()=="NO PICTURE")
        {
            ProfilImg!!.setImageResource(R.drawable.avtar)
        }
        else
        {
            val filename2 = mSharedPref.getString(PHOTOR, "").toString()
            Log.e("image",filename2)
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F$filename2?alt=media"
            Glide.with(this)
                .load(path)
                .into(ProfilImg)
        }
        val apiInterface = ApiInterface.create()

        val map: HashMap<String, String> = HashMap()

        map["category"] = mSharedPref.getString(CATEGORYR,"").toString()
        map["idrecruiter"] = mSharedPref.getString(IDR,"").toString()
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val anyMutableList1 = mutableListOf("bezkoder.com", "2019")
                val response = apiInterface.fetchcallsbyrecruiter(map)
                Log.e("body",map.toString())
                Log.e("response", response.body().toString())
                Log.e("response", response.toString())
                withContext(Dispatchers.Main){
                    val calls = response.body()
                    if ( response.code()== 200){
                        progBar.visibility = View.GONE
                        fab.visibility = View.VISIBLE
                        val adapter = response.body()?.let { CallsAdapterRecruiter(it) }
                        // This will pass the ArrayList to our Adapter


                        // Setting the Adapter with the recyclerview

                        rv_callsR.adapter = adapter
                        rv_callsR.apply {
                            layoutManager = GridLayoutManager(context, 2)
                        }
                    }else{
                        progBar.visibility = View.GONE
                        fab.visibility = View.VISIBLE
                        Toast.makeText(this@CallsRecruiter, "no data", Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: Exception){
                Log.e("exeption",e.toString())

            }


        }
    }
}