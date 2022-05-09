package com.wael.tyvolunty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wael.tyvolunty.models.Volunteer
import com.wael.tyvolunty.utilis.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import java.util.regex.Pattern

class Signup : AppCompatActivity() {

    lateinit var txtfullname: TextInputEditText
    lateinit var txtLayoutFullName: TextInputLayout

    lateinit var txtemail: TextInputEditText
    lateinit var txtLayoutEmail: TextInputLayout

    lateinit var txtpassword: TextInputEditText
    lateinit var passwordlayout: TextInputLayout

    lateinit var txtphone: TextInputEditText
    lateinit var phone: TextInputLayout

    lateinit var txtadresse: TextInputEditText
    lateinit var adresse: TextInputLayout

    lateinit var txtage: TextInputEditText
    lateinit var txtLayoutAge: TextInputLayout

    private lateinit var btncreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btncreate = findViewById(R.id.btnNext)

        txtLayoutFullName = findViewById(R.id.txtLayoutFullName)
        txtfullname = findViewById(R.id.txtFullName)

        txtLayoutEmail = findViewById(R.id.txtLayoutEmail)
        txtemail = findViewById(R.id.txtEmail)

        passwordlayout = findViewById(R.id.passwordlayout)
        txtpassword = findViewById(R.id.password)


        phone = findViewById(R.id.phone)
        txtphone = findViewById(R.id.txtphone)

        adresse = findViewById(R.id.adresse)
        txtadresse = findViewById(R.id.txtadresse)

        txtLayoutAge = findViewById(R.id.txtLayoutAge)
        txtage = findViewById(R.id.txtAge)




        btncreate.setOnClickListener {
           doLogin()
        }
    }
    private fun doLogin(){
        if (validate()){
            val apiInterface = ApiInterface.create()
            val map: HashMap<String, String> = HashMap()

            map["username"] = txtfullname.text.toString()
            map["email"] = txtemail.text.toString()
            map["password"] = txtpassword.text.toString()
            map["phone"] = txtphone.text.toString()
            map["age"] = txtage.text.toString()

            apiInterface.Signup(map).enqueue(object : Callback<Volunteer> {

                override fun onResponse(call: Call<Volunteer>, response: Response<Volunteer>) {

                    val user = response.body()
                    Log.d("user","y")
                    if (user != null){
                        val intent = Intent(this@Signup, Login::class.java)
                        startActivity(intent)

                    }else{

                        txtLayoutFullName.error = "error"

                    }
                }

                override fun onFailure(call: Call<Volunteer>, t: Throwable) {
                    Toast.makeText(this@Signup, "Connexion error!", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
    private fun validate(): Boolean {
        btncreate.error = null
        txtLayoutFullName.error = null

        if (txtfullname.text!!.isEmpty()){
            txtLayoutFullName.error = "must not be empty"
            return false
        }
        if (txtfullname.text!!.count()<5){
            txtLayoutFullName.error = "must contains at least 5 caracters"
            return false
        }

        if (txtemail.text!!.isEmpty() ){
            txtLayoutEmail.error = "must not be empty"
            return false
        }
        if (!checkEmail(txtemail.text!!.toString())){
            txtLayoutEmail.error = "must be valid email"
            return false
        }else{
            txtLayoutEmail.error = null
        }

        if (txtpassword.text!!.isEmpty()){
            passwordlayout.error = "must not be empty"
            return false
        }
        if (txtpassword.text!!.count()<5){
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
        if (txtage.text!!.isEmpty()){
            txtLayoutAge.error = "must not be empty"
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