package com.wael.tyvolunty

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_detail_call.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.profilePic
import kotlinx.android.synthetic.main.item_bulle.*
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class DetailCall : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_call)
        restonamedetail.setText(intent.getStringExtra("name").toString())
        description.setText(intent.getStringExtra("description").toString())
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F2022_04_27_01_01_21?alt=media&token=73554f53-d1cd-4839-8543-f555c21406f1"
        Glide.with(this)
            .load(path)
            .into(restodetail_img)
        retour.setOnClickListener {
            finish()
        }
        qrcode.setOnClickListener {


                val intent = Intent(this, popover::class.java)
                intent.putExtra("popuptitle", "Error")
                intent.putExtra("popuptext", "Sorry, that email address is already used!")
                intent.putExtra("popupbtn", "OK")

            intent.putExtra("darkstatusbar", false)
            intent.putExtra("name", intent.getStringExtra("name").toString())
            intent.putExtra("description", intent.getStringExtra("description").toString())
            intent.putExtra("category", intent.getStringExtra("description").toString())
                startActivity(intent)

        }
        reserve.setOnClickListener {

            val apiInterface = ApiInterface.create()

            val map: HashMap<String, String> = HashMap()

            map["callId"] = intent.getStringExtra("id").toString()
            map["idV"] = mSharedPref.getString(ID,"").toString()



            Log.e("map", map.toString())
            CoroutineScope(Dispatchers.IO).launch {

                try {

                    val response = apiInterface.postforcall(map)
                    Log.e("responseServeur", response.toString())
                    withContext(Dispatchers.Main){

                        if ( response.code()==200){

                            Toast.makeText(
                                this@DetailCall,
                                "posted with success",
                                Toast.LENGTH_SHORT
                            ).show()
                            reserve.isEnabled = false


                        }else{
                            Toast.makeText(
                                this@DetailCall,
                                "already posted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }catch (e: Exception){
                    Log.e("response",e.message.toString())
                }


            }
        }

    }

}