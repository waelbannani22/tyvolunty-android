package com.wael.tyvolunty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.wael.tyvolunty.adapters.ListpendingAdapter
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_accept_decline.*
import kotlinx.android.synthetic.main.activity_calls.*
import kotlinx.android.synthetic.main.activity_calls.progBar
import kotlinx.android.synthetic.main.activity_pendinglist.*
import kotlinx.android.synthetic.main.item_bulle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class AcceptDecline : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_decline)
        accept.isEnabled = false
        decline.isEnabled = false
        decline.visibility =View.GONE
        accept.visibility =View.GONE
        progressBar3.visibility =View.VISIBLE
        var emailV =""
        var imageV=""

        val apiInterface = ApiInterface.create()

        val map: HashMap<String, String> = HashMap()
        if (intent.getStringExtra("status").toString() =="pending"){
            accept.isEnabled = true
            decline.isEnabled = true
            decline.visibility =View.VISIBLE
            accept.visibility =View.VISIBLE
        }
        map["id"] = intent.getStringExtra("idV").toString()
        CoroutineScope(Dispatchers.IO).launch {

            try {

                val response = apiInterface.fetchvolunteerbyid(map)
                Log.e("body",map.toString())
                Log.e("response", response.body().toString())
                Log.e("response", response.toString())
                withContext(Dispatchers.Main){
                    val user = response.body()
                    if ( response.code()== 200){
                        if (user != null) {
                            progressBar3.visibility = View.GONE
                            username.setText(user.username)
                            email.setText(user.email)
                            emailV=user.email
                            phone.setText(user.phone)
                            if (user.photo !=null){
                                bindimage(user.photo)
                            }else{
                                bindimage("")
                            }


                        }


                    }else{

                        progressBar3.visibility = View.GONE
                        Toast.makeText(this@AcceptDecline, "no data", Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: Exception){
                Log.e("exeption",e.toString())

            }


        }

        //accept**************************
        accept.setOnClickListener {
            val apiInterface = ApiInterface.create()

            val map: HashMap<String, String> = HashMap()

            map["callId"] = intent.getStringExtra("callId").toString()
            map["idv"] = intent.getStringExtra("idV").toString()
            map["email"] = emailV
            CoroutineScope(Dispatchers.IO).launch {

                try {

                    val response = apiInterface.acceptVolunteer(map)
                    Log.e("body",map.toString())
                    Log.e("response", response.body().toString())
                    Log.e("response", response.toString())
                    withContext(Dispatchers.Main){
                        val user = response.body()
                        if ( response.code()== 200){
                            if (user != null) {

                                val intent = Intent(this@AcceptDecline, AccueilRecruiter::class.java)
                                startActivity(intent)
                                finish()


                            }


                        }else{


                            Toast.makeText(this@AcceptDecline, "failure", Toast.LENGTH_SHORT).show()

                        }
                    }

                }catch (e: Exception){
                    Log.e("exeption",e.toString())

                }


            }
        }
        //decline*******************
        decline.setOnClickListener {
            val apiInterface = ApiInterface.create()

            val map: HashMap<String, String> = HashMap()

            map["callId"] = intent.getStringExtra("callId").toString()
            map["idv"] = intent.getStringExtra("idV").toString()

            CoroutineScope(Dispatchers.IO).launch {

                try {

                    val response = apiInterface.declineVolunteer(map)
                    Log.e("body",map.toString())
                    Log.e("response", response.body().toString())
                    Log.e("response", response.toString())
                    withContext(Dispatchers.Main){
                        val user = response.body()
                        if ( response.code()== 200){
                            if (user != null) {

                                val intent = Intent(this@AcceptDecline, AccueilRecruiter::class.java)
                                startActivity(intent)
                                finish()

                            }


                        }else{


                            Toast.makeText(this@AcceptDecline, "failure", Toast.LENGTH_SHORT).show()

                        }
                    }

                }catch (e: Exception){
                    Log.e("exeption",e.toString())

                }


            }
        }

    }
    fun bindimage(imgs:String){
        if(imgs=="") {
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F%E2%80%94Pngtree%E2%80%94volunteer%20cleaning%20up%20garbage%20concept_6689209.png?alt=media&token=4e085d63-bbd7-4780-9f45-669114c6ba07"
            Glide.with(this)
                .load(path)
                .into(img)

        }
        else
        {

            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F$imgs?alt=media"
            Glide.with(this)
                .load(path)
                .into(img)
        }
    }


}