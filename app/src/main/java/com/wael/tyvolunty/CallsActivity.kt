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
import com.wael.tyvolunty.adapters.CallsAdapter
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_calls.*
import kotlinx.android.synthetic.main.item_bulle.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class CallsActivity : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calls)
        progBar.visibility =View.VISIBLE
        //fab.visibility = View.GONE
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        linearLayoutManager = LinearLayoutManager(this)
        rv_calls.layoutManager = linearLayoutManager
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        if(mSharedPref.getString(PHOTO, "").toString()=="NO PICTURE")
        {
            ProfilImg!!.setImageResource(R.drawable.avtar)
        }
        else
        {
            val filename2 = mSharedPref.getString(PHOTO, "").toString()
            Log.e("image",filename2)
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F$filename2?alt=media"
            Glide.with(this)
                .load(path)
                .into(ProfilImg)
        }
        val apiInterface = ApiInterface.create()

        val map: HashMap<String, String> = HashMap()

        map["category"] = mSharedPref.getString(CATEGORY,"").toString()
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val anyMutableList1 = mutableListOf("bezkoder.com", "2019")
                val response = apiInterface.fetchbycategory(map)
                Log.e("body",map.toString())
                Log.e("response", response.body().toString())
                Log.e("response", response.toString())
                withContext(Dispatchers.Main){
                    val calls = response.body()
                    if ( response.code()== 200){
                        progBar.visibility =View.GONE
                        //fab.visibility = View.VISIBLE
                        val adapter = response.body()?.let { CallsAdapter(it) }
                        // This will pass the ArrayList to our Adapter


                        // Setting the Adapter with the recyclerview

                        rv_calls.adapter = adapter
                        rv_calls.apply {
                            layoutManager = GridLayoutManager(context, 2)
                        }
                    }else{
                        progBar.visibility =View.GONE
                        //fab.visibility = View.VISIBLE
                        Toast.makeText(this@CallsActivity, "no data", Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: Exception){
                Log.e("exeption",e.toString())

            }


        }
    }
}