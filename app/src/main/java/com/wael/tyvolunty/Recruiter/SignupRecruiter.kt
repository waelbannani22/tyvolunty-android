package com.wael.tyvolunty.Recruiter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wael.tyvolunty.*
import com.wael.tyvolunty.models.Volunteer
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_forgetpassword.view.*
import kotlinx.android.synthetic.main.activity_signin_recruiter.*
import kotlinx.android.synthetic.main.activity_signup_recruiter.*
import kotlinx.android.synthetic.main.activity_signup_recruiter.txtEmail
import kotlinx.android.synthetic.main.activity_signup_recruiter.txtLayoutEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.regex.Pattern

class SignupRecruiter : AppCompatActivity() {






    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_recruiter)








        btnNextR!!.setOnClickListener {
            doLogin()
        }
    }
    private fun doLogin(){
        if (validate()){
            val apiInterface = ApiInterface.create()
            val map: HashMap<String, String> = HashMap()

            map["name"] = txtFullName.text.toString()
            map["email"] = txtEmail.text.toString()
            map["password"] = password.text.toString()
            map["phone"] = txtphone.text.toString()
            map["organisation"] = txtadresse.text.toString()

            CoroutineScope(Dispatchers.IO).launch {

                try {
                    Log.e("response", "hi before")
                    val response = apiInterface.signupRecruiter(map)
                    Log.e("response", response.body().toString())
                    withContext(Dispatchers.Main){

                        if ( response.isSuccessful){
                            val intent = Intent(this@SignupRecruiter, SigninRecruiter::class.java)
                            startActivity(intent)
                            finish()

                        }else{
                            Log.e("response","error f")
                        }
                    }

                }catch (e: Exception){
                    Log.e("response","error f")
                }


            }
        }
    }
    private fun validate(): Boolean {
        btnNextR.error = null
        txtLayoutFullName.error = null

        if (txtFullName.text!!.isEmpty()){
            txtLayoutFullName.error = "must not be empty"
            return false
        }
        if (txtFullName.text!!.count()<5){
            txtLayoutFullName.error = "must contains at least 5 caracters"
            return false
        }

        if (txtEmail.text!!.isEmpty() ){
            txtLayoutEmail.error = "must not be empty"
            return false
        }
        if (!checkEmail(txtEmail.text!!.toString())){
            txtLayoutEmail.error = "must be valid email"
            return false
        }else{
            txtLayoutEmail.error = null
        }

        if (password.text!!.isEmpty()){
            passwordlayout.error = "must not be empty"
            return false
        }
        if (password.text!!.count()<5){
            txtLayoutFullName.error = " password must contains at least 5 caracters"
            return false
        }
        if (txtphone.text!!.isEmpty()){
            phone.error = "must not be empty"
            return false
        }
        if (txtphone.text!!.count()<8){
            phone.error = "phone number must be valid "
            return false
        }
        if (txtadresse.text!!.isEmpty()){
            adresse.error = "must not be empty"
            return false
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