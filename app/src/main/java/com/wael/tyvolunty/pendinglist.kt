package com.wael.tyvolunty

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wael.tyvolunty.adapters.CallsAdapter
import com.wael.tyvolunty.adapters.ListpendingAdapter
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_calls.*
import kotlinx.android.synthetic.main.activity_calls.progBar
import kotlinx.android.synthetic.main.activity_pendinglist.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class pendinglist : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendinglist)
        progBar.visibility = View.VISIBLE
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        linearLayoutManager = LinearLayoutManager(this)

        listpending.layoutManager = linearLayoutManager
        val apiInterface = ApiInterface.create()

        val map: HashMap<String, String> = HashMap()

        map["callId"] = intent.getStringExtra("id").toString()
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = apiInterface.fetchcallbyid(map)
                Log.e("body",map.toString())
                Log.e("response", response.body().toString())
                Log.e("response", response.toString())
                withContext(Dispatchers.Main){
                    val calls = response.body()
                    if ( response.code()== 200){
                        progBar.visibility =View.GONE
                        //fab.visibility = View.VISIBLE
                        val adapter = response.body()?.let { ListpendingAdapter(it) }
                        // This will pass the ArrayList to our Adapter


                        // Setting the Adapter with the recyclerview

                        listpending.adapter = adapter
                        listpending.apply {
                            layoutManager = GridLayoutManager(context, 1)
                        }
                    }else{
                        progBar.visibility =View.GONE
                        //fab.visibility = View.VISIBLE

                        Toast.makeText(this@pendinglist, "no data", Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: Exception){
                Log.e("exeption",e.toString())

            }


        }
    }
    fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(this, message, duration).show()
    }
}