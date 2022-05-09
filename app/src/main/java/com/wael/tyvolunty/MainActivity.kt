package com.wael.tyvolunty

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.item_age.*
import kotlinx.android.synthetic.main.item_age.age
import kotlinx.android.synthetic.main.item_agem.*
import kotlinx.android.synthetic.main.item_infom.*
import kotlinx.android.synthetic.main.item_mailm.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private lateinit var txtname: TextView
    private lateinit var txtemail: TextView
    private lateinit var txtage: TextView
    private lateinit var phone: TextView
    private lateinit var confirm: Button
    private lateinit var upload: Button
    private var profilePic: ShapeableImageView? = null
    private var selectedImageUri: Uri? = null
    lateinit var storage: FirebaseStorage
    val formater = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val fileName = formater.format(now)
    private lateinit var mSharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //binding
        mSharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        storage = Firebase.storage
        txtname = findViewById(R.id.name)
        txtemail = findViewById(R.id.email)
        txtage = findViewById(R.id.age)
        phone = findViewById(R.id.number)
        profilePic = findViewById(R.id.profilePic1)
        confirm = findViewById(R.id.confirm)
        upload = findViewById(R.id.upload)
        if(mSharedPref.getString(PHOTO, "").toString()=="NO PICTURE")
        {
            profilePic!!.setImageResource(R.drawable.slider)
        }
        else
        {
            val filename2 = mSharedPref.getString(PHOTO, "").toString()
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F$filename2?alt=media"
            Glide.with(this)
                .load(path)
                .into(profilePic)
        }


        //listners

        txtname.setText( mSharedPref.getString(USERNAME, "").toString())
        phone.setText( mSharedPref.getString(PHONE, "").toString())
        txtemail.setText( mSharedPref.getString(EMAIL, "").toString())
        txtage.setText(mSharedPref.getString(AGE, "").toString())

        profilePic!!.setOnClickListener{
            openGallery()
        }
        confirm.setOnClickListener {
            if (validate()){
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Saving Changes ...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                val apiInterface = ApiInterface.create()
                val map: HashMap<String, String> = HashMap()
                val id = mSharedPref.getString(ID, "").toString()
                map["username"] = txtname.text.toString()
                map["phone"] = phone.text.toString()
                map["age"] = txtage.text.toString()
                map["id"] = id
                if (selectedImageUri == null) {
                    map["image"] = mSharedPref.getString(PHOTO, "").toString()
                }
                else
                    map["image"] = fileName.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = apiInterface.updateVolunteers(map)
                        withContext(Dispatchers.Main) {
                            if ( response.code()==200){
                                mSharedPref.edit().apply{
                                    val user = response.body()
                                    Log.e("response",user.toString())
                                    if (user != null) {
                                        putString(USERNAME, user.username)
                                        putString(AGE, user.age)
                                        putString(PHONE, user.phone)
                                        putString(PHOTO, user.photo)
                                    }

                                }.apply()
                                if(progressDialog.isShowing)
                                {
                                    progressDialog.dismiss()
                                }
                                val intent = Intent(this@MainActivity, Accueil::class.java)
                                startActivity(intent)
                                finish()


                            }else{
                                if(progressDialog.isShowing)
                                {
                                    progressDialog.dismiss()
                                }
                                Toast.makeText(
                                    this@MainActivity,
                                    "error ocured",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    } catch(e: Exception){
                        Log.e("response",e.message.toString())
                    }
                    apiInterface.updateVolunteers(map)
                }
            }
        }
        upload!!.setOnClickListener {
            uploadImage()
        }

    }
    private fun uploadImage()
    {
        if (selectedImageUri == null) {
            Toast.makeText(this@MainActivity,"Please Select Picture", Toast.LENGTH_SHORT).show()
        }
        else
        {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Uploading Image ...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            val storageReference = FirebaseStorage.getInstance().reference.child("images/$fileName")
            Log.e("firebase",storageReference.toString())
           Log.e("urllll", storageReference.downloadUrl.toString())
            Log.e("image ",selectedImageUri!!.toString())
            storageReference.putFile(selectedImageUri!!).
            addOnSuccessListener {
                profilePic!!.setImageURI(selectedImageUri)
                if(progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                Toast.makeText(this,"Successfuly uploaded", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                if(progressDialog.isShowing)
                {
                    progressDialog.dismiss()
                }
                Toast.makeText(this,"Sorry", Toast.LENGTH_SHORT).show()

            }
        }

    }
    private fun logout() {
        mSharedPref.edit().clear().apply()
        val intent = Intent(this@MainActivity, Login::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu1, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.menu.menu1 -> {
                val intent = Intent(this@MainActivity, empty::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }
    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100 ) {
            selectedImageUri = data?.data
            profilePic!!.setImageURI(selectedImageUri)
        }
    }
    private fun validate(): Boolean {
        confirm.error = null
        lytnumber.error = null


        if (number.text!!.isEmpty()){
            lytnumber.error = "must not be empty"
            return false
        }
        if (number.text!!.count() <2){
            lytage.error = "must be over 5 caracters"
            return false
        }
        if (age.text!!.count() <2){
            lytage.error = "must valid age"
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