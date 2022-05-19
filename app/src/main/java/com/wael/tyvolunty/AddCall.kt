package com.wael.tyvolunty

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import android.widget.AdapterView.OnItemSelectedListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.wael.tyvolunty.image
import com.wael.tyvolunty.txtDate
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.activity_add_call.*
import kotlinx.android.synthetic.main.activity_detail_call.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


private var selectedImageUri: Uri? = null

lateinit var image: ImageView
lateinit var txtDate: TextInputEditText
private lateinit var mSharedPref: SharedPreferences

lateinit var storage: FirebaseStorage
val formater = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
val now = Date()
val fileName = formater.format(now)
class AddCall : AppCompatActivity(), OnItemSelectedListener {

    var category = arrayOf<String?>("ANIMAL", "SENOR",
        "CHILDREN", "COMMUNITY",
        "ENVIRONMENT", "HEALTH","HOMELESS","DISABILITY","EDUCATION",)

    private val startForResultOpenGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data!!.data
            image.setImageURI(selectedImageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_call)
        mSharedPref = getSharedPreferences(PREF_NAMER, MODE_PRIVATE);
        storage = Firebase.storage
        val DatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
        DatePicker.addOnPositiveButtonClickListener {
            txtDate.setText(DatePicker.headerText.toString())
        }
        txtDate.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                DatePicker.show(supportFragmentManager, "START_DATE")
            }else{
                DatePicker.dismiss()
            }
        }
        image.setOnClickListener {
            openGallery()
        }
        upload!!.setOnClickListener {
            uploadImage()
        }
        btnLogin.setOnClickListener {
            if (validate()){
                val apiInterface = ApiInterface.create()

                val map: HashMap<String, String> = HashMap()

                map["name"] = txtNameOfExperience.text.toString()
                map["recruiter"] = mSharedPref.getString(ID,"").toString()
                map["city"] = txtAddress.text.toString()
                map["dateBegin"] = txtDate.text.toString()
                map["description"] = txtDescription.text.toString()
                map["ageGroup"] = txtAge.text.toString()
                map["category"] = mSharedPref.getString(CATEGORYR,"").toString()
                map["photo"]= fileName.toString()


                Log.e("map", map.toString())
                CoroutineScope(Dispatchers.IO).launch {
                    btnLogin.isEnabled = false
                    try {

                        val response = apiInterface.addcall(map)
                        Log.e("responseServeur", response.toString())
                        withContext(Dispatchers.Main){

                            if ( response.code()==200){

                                Toast.makeText(
                                    this@AddCall,
                                    "added  success",
                                    Toast.LENGTH_SHORT
                                ).show()
                                btnLogin.isEnabled = false
                                val intent = Intent(this@AddCall, CallsRecruiter::class.java)
                                startActivity(intent)


                            }else{
                                btnLogin.isEnabled = true
                                Toast.makeText(
                                    this@AddCall,
                                    "failed to save call",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        }

                    }catch (e: Exception){
                        Log.e("response",e.message.toString())
                    }


                }
            }
        }


    }
    private fun uploadImage()
    {
        if (selectedImageUri == null) {
            Toast.makeText(this@AddCall,"Please Select Picture", Toast.LENGTH_SHORT).show()
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
                image!!.setImageURI(selectedImageUri)
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

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startForResultOpenGallery.launch(intent)
    }
    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View, position: Int,
                                id: Long) {
        // make toastof name of course
        // which is selected in spinner
        Toast.makeText(applicationContext,
            category[position],
            Toast.LENGTH_LONG)
            .show()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    private fun validate(): Boolean {
        btnLogin.error = null
        txtLayoutNameOfExperience.error = null

        if (selectedImageUri == null) {
            Toast.makeText(this@AddCall,"Please Select Picture", Toast.LENGTH_SHORT).show()
            return false
        }
        if (txtNameOfExperience.text!!.isEmpty()){
            txtLayoutNameOfExperience.error = "must not be empty"
            return false
        }
        if (txtNameOfExperience.text!!.count()<5){
            txtLayoutNameOfExperience.error = "must contains at least 5 caracters"
            return false
        }

        if (txtAge.text!!.isEmpty() ){
            txtLayoutAge.error = "must not be empty"
            return false
        }


        if (txtDate.text!!.isEmpty()){
            txtLayoutDate.error = "must not be empty"
            return false
        }
        if (txtAddress.text!!.count()<4){
            txtLayoutAddress.error = "  must contains at least 5 caracters"
            return false
        }
        if (txtCity.text!!.isEmpty()){
            txtLayoutCity.error = "must not be empty"
            return false
        }
        if (txtDescription.text!!.count()<8){
            txtLayoutDescription.error = "  must contains at least 8 caracters "
            return false
        }


        return true
    }
}