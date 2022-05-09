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
import com.wael.tyvolunty.Recruiter.SignupRecruiter
import com.wael.tyvolunty.models.Recruiter
import kotlinx.android.synthetic.main.activity_signin_recruiter.*


private lateinit var txtYourAccount: TextView
private lateinit var imageView: ImageView
private lateinit var txtCreate: TextView
private lateinit var txtLayoutEmail: TextInputLayout
private lateinit var txtEmail: TextInputEditText



private lateinit var txtPassword: TextInputEditText

private lateinit var txtForgetPassword: TextView
private lateinit var txtDontHaveAccount: TextView
private lateinit var txtSignUp: TextView
private lateinit var btnLogin: MaterialButton

//shared preff
const val PREF_NAMER = "DATA_PREF"
const val IDR = "ID"
const val USERNAMER = "USERNAME"
const val AGER = "AGE"
const val EMAILR = "EMAIL"
const val PHONER = "PHONE"
const val PHOTOR = "NO PICTURE"
const val DESCRIPTIONR = "DESCRIPTION"
const val CATEGORYR = "CATEGORY"
const val NAMER = "NAMER"



class SigninRecruiter : AppCompatActivity() {

    private lateinit var txtYourAccount: TextView
    private lateinit var imageView: ImageView
    private lateinit var txtCreate: TextView
    private lateinit var txtLayoutEmail: TextInputLayout
    private lateinit var txtEmail: TextInputEditText



    private lateinit var txtPassword: TextInputEditText

    private lateinit var txtForgetPassword: TextView
    private lateinit var txtDontHaveAccount: TextView

    private lateinit var btnLogin: MaterialButton

    private lateinit var mSharedPref: SharedPreferences

    private  lateinit var spinner :ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_recruiter)

        mSharedPref = getSharedPreferences(PREF_NAMER, MODE_PRIVATE);

        btnLogin = findViewById(R.id.btnLogin)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        txtDontHaveAccount = findViewById(R.id.txtDontHaveAccount)
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail)

        txtEmail = findViewById(R.id.txtEmail)

        txtPassword = findViewById(R.id.txtPassword)
        spinner = findViewById(R.id.progress1)
        spinner.visibility = View.GONE

        btnLogin.setOnClickListener {
            doLogin()
        }
        txtSignUpR.setOnClickListener {
            val intent = Intent(this, SignupRecruiter::class.java)
            startActivity(intent)
        }
        txtForgetPassword.setOnClickListener {


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

                apiInterface.signinRecruiter(map).enqueue(object : Callback<Recruiter> {

                    override fun onResponse(call: Call<Recruiter>, response: Response<Recruiter>) {

                        val user = response.body()

                        Log.e("user : ", user.toString())
                        Log.e("re : ", response.isSuccessful.toString())
                        if (response.isSuccessful && user != null) {
                            mSharedPref.edit().apply {
                                putString(IDR, user._id)
                                putString(NAMER, user.name)
                                putString(EMAILR, user.email)
                                putString(DESCRIPTION, user.organization)
                                putString(PHONER, user.phone)
                                putString(PHOTOR, user.photo)


                            }.apply()

                            val intent = Intent(this@SigninRecruiter, AccueilRecruiter::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            spinner.visibility = View.GONE
                            Toast.makeText(
                                this@SigninRecruiter,
                                "invalid email or password",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<Recruiter>, t: Throwable) {
                        spinner.visibility = View.GONE
                        Toast.makeText(this@SigninRecruiter, "invalid email or password", Toast.LENGTH_SHORT).show()
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