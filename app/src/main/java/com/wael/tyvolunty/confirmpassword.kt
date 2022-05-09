package com.wael.tyvolunty

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class confirmpassword : AppCompatActivity() {
    private lateinit var txtLayoutpassword: TextInputLayout
    private lateinit var txtpassword: TextInputEditText

    private lateinit var txtLayoutpasswordConfirm: TextInputLayout
    private lateinit var txtpasswordConfirm: TextInputEditText
    private lateinit var btnconfirm: Button
    private lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmpassword)
        //bind
        txtLayoutpassword = findViewById(R.id.txtLayoutPassword)
        txtpassword = findViewById(R.id.txtPassword)
        mSharedPref = getSharedPreferences(PREF_NAME1, MODE_PRIVATE);
        txtLayoutpasswordConfirm = findViewById(R.id.txtLayoutConfirmPassword)
        txtpasswordConfirm = findViewById(R.id.txtConfirmPassword)
        btnconfirm = findViewById(R.id.btnConfirm2)
        //listners
        btnconfirm.setOnClickListener{
            compares()
        }
    }
    fun compares(){
        Log.e("pass", txtpassword.text.toString())
        Log.e("passconfirm", txtpasswordConfirm.text.toString())
        if( txtpassword.text.toString() == txtpasswordConfirm.text.toString()){
            txtLayoutpassword.error = null
            txtLayoutpasswordConfirm.error =  null

            //webservice
            val apiInterface = ApiInterface.create()

            val map: HashMap<String, String> = HashMap()

            map["password"] = txtpassword.text.toString()

            var id = mSharedPref.getString("ID1","")
            Log.e("response", id.toString())
            Log.e("map", map.toString())
            CoroutineScope(Dispatchers.IO).launch {

                try {
                    Log.e("responseServeur", "outside api")
                    val response = apiInterface.change("625644002eadb8aa26c64854", map)
                    Log.e("responseServeur", response.toString())
                    withContext(Dispatchers.Main){

                        if ( response.code()==200){

                            val intent = Intent(this@confirmpassword, Login::class.java)
                            startActivity(intent)
                            finish()


                        }else{
                            Toast.makeText(
                                this@confirmpassword,
                                "error ocured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }catch (e: Exception){
                    Log.e("response",e.message.toString())
                }


            }


        }else{
            txtLayoutpassword.error = "password should matches"
            txtLayoutpasswordConfirm.error =  "password should matches"
            Toast.makeText(
                this@confirmpassword,
                "make them the same",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}