package com.wael.tyvolunty

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wael.tyvolunty.models.Volunteer
import com.wael.tyvolunty.utilis.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.widget.ProgressBar
import androidx.core.view.isVisible


private lateinit var txtYourAccount: TextView
private lateinit var imageView: ImageView
private lateinit var txtCreate: TextView
private lateinit var txtLayoutEmail: TextInputLayout
private lateinit var txtEmail: TextInputEditText

private lateinit var txtLayoutPassword: TextInputLayout

private lateinit var txtPassword: TextInputEditText

private lateinit var txtForgetPassword: TextView
private lateinit var txtDontHaveAccount: TextView
private lateinit var txtSignUp: TextView
private lateinit var btnLogin: MaterialButton

//shared preff
const val PREF_NAME = "DATA_PREF"
const val ID = "ID"
const val USERNAME = "USERNAME"
const val AGE = "AGE"
const val EMAIL = "EMAIL"
const val PHONE = "PHONE"
const val PHOTO = "NO PICTURE"
const val DESCRIPTION = "DESCRIPTION"
const val CATEGORY = "CATEGORY"




class Login : AppCompatActivity() {

    private lateinit var txtYourAccount: TextView
    private lateinit var imageView: ImageView
    private lateinit var txtCreate: TextView
    private lateinit var txtLayoutEmail: TextInputLayout
    private lateinit var txtEmail: TextInputEditText

    private lateinit var txtLayoutPassword: TextInputLayout

    private lateinit var txtPassword: TextInputEditText

    private lateinit var txtForgetPassword: TextView
    private lateinit var txtDontHaveAccount: TextView
    private lateinit var txtSignUp: TextView
    private lateinit var btnLogin: MaterialButton

    private lateinit var mSharedPref: SharedPreferences

    private  lateinit var spinner :ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        btnLogin = findViewById(R.id.btnLogin)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        txtDontHaveAccount = findViewById(R.id.txtDontHaveAccount)
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail)
        txtEmail = findViewById(R.id.txtEmail)
        txtSignUp = findViewById(R.id.txtSignUp)
        txtPassword = findViewById(R.id.txtPassword)
        spinner = findViewById(R.id.progress1)
        spinner.visibility = View.GONE

        btnLogin.setOnClickListener {
            doLogin()
        }
        txtSignUp.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }
        txtForgetPassword.setOnClickListener {
            val intent = Intent(this, forgetpassword::class.java)
            startActivity(intent)

        }

    }
    private fun doLogin(){
        if (validate()) {
            spinner.visibility = View.VISIBLE

            val apiInterface = ApiInterface.create()

            val map: HashMap<String, String> = HashMap()

            map["email"] = txtEmail.text.toString()
            map["password"] = txtPassword.text.toString()

            CoroutineScope(Dispatchers.Main).launch {

                apiInterface.login(map).enqueue(object : Callback<Volunteer> {

                    override fun onResponse(call: Call<Volunteer>, response: Response<Volunteer>) {

                        val user = response.body()

                        Log.e("user : ", user.toString())
                        Log.e("re : ", response.isSuccessful.toString())
                        if (response.isSuccessful && user != null) {
                            mSharedPref.edit().apply {
                                putString(ID, user._id)
                                putString(USERNAME, user.username)
                                putString(AGE, user.age)
                                putString(EMAIL, user.email)
                                putString(DESCRIPTION, user.description)
                                putString(PHONE, user.phone)
                                putString(PHOTO, user.photo)


                            }.apply()

                            val intent = Intent(this@Login, Accueil::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                         spinner.visibility = View.GONE
                            Toast.makeText(
                                this@Login,
                                "invalid email or password",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<Volunteer>, t: Throwable) {
                        Toast.makeText(this@Login, "invalid email or password", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }
    }
    private fun validate(): Boolean {
        btnLogin.error = null
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
        if (txtPassword.text!!.isEmpty()){
            txtLayoutPassword.error = "must not be empty"
            return false
        }
        if (txtPassword.text!!.count() <5){
            txtLayoutPassword.error = "password must contains at least 5 caraters"
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