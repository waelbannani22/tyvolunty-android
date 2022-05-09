package com.wael.tyvolunty

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.beust.klaxon.Klaxon
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import com.wael.tyvolunty.models.Volunteer
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_forgetpassword.*
import kotlinx.android.synthetic.main.activity_forgetpassword.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.util.ArrayList
import java.util.regex.Pattern
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

const val PREF_NAME1 = "DATA_PREF1"
const val ID1 = "ID1"
const val CODE = "CODE"
var code1 = ""
var id =""
class forgetpassword : AppCompatActivity() {

    private lateinit var txtLayoutEmail: TextInputLayout
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtLayoutcode: TextInputLayout
    private lateinit var txtcode: TextInputEditText
    private lateinit var btnemail: Button
    private lateinit var btnconfirm: Button
    private lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpassword)
        //binding
        mSharedPref = getSharedPreferences(PREF_NAME1, MODE_PRIVATE);
        txtEmail = findViewById(R.id.txtEmail)
        txtcode = findViewById(R.id.txtCode)
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail)
        txtLayoutcode = findViewById(R.id.txtLayoutCode)
        btnemail = findViewById(R.id.btnSend)
        txtcode.isEnabled = false
         btnconfirm = findViewById(R.id.btnConfirm)

        //listners
        btnemail.setOnClickListener{
            dosend()
        }
        btnconfirm.setOnClickListener{
            tapcode()
        }



    }
    private fun dosend()
    {
        if(validate()) {
            val apiInterface = ApiInterface.create()

            val map: HashMap<String, String> = HashMap()

            map["email"] = txtEmail.text.toString()

            CoroutineScope(Dispatchers.IO).launch {

                try {

                    val response = apiInterface.Sendmailvolunteer(map)
                    Log.e("response", response.body().toString())
                    withContext(Dispatchers.Main){

                        if ( response.isSuccessful){
                           txtcode.isEnabled = true

                          //  Log.e("response", response.body()?.get("code").toString())
                            var z =  response.body()?.get("code").toString().length
                            var x = response.body()?.get("code").toString()
                            var i = x.substring(1, z - 1);

                            var za =  response.body()?.get("id").toString().length
                            var xa = response.body()?.get("id").toString()
                            var ia = xa.substring(1, za - 1);
                            Log.e("response", i)
                            code1 = i
                            id = ia
                            mSharedPref.edit().apply {
                                putString(CODE, i)
                                putString(ID1, ia)


                            }.apply()
                        }else{
                            Log.e("response","error f")
                        }
                    }

                }catch (e:Exception){
                    Log.e("response","error f")
                }


            }



        }

    }

    private fun tapcode()
    {


            Log.e("response1",code1)
            Log.e("tapped",txtcode.text.toString())
            if ( code1 ==txtcode.text.toString() ){
                val intent = Intent(this@forgetpassword, confirmpassword::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(
                    this@forgetpassword,
                    "password should matches",
                    Toast.LENGTH_SHORT
                ).show()
            }





    }
     fun validate(): Boolean {

        txtLayoutEmail.error = null

        if (txtEmail.text!!.isEmpty()){
            txtLayoutEmail.error = "must not be empty"
            return false
        }
        if (!checkEmail(txtEmail.text!!.toString())){
            txtLayoutEmail.error = "must not be valid email"
            return false
        }else{
            txtLayoutEmail.error = null
        }



        return true
    }
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}