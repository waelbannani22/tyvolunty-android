package com.wael.tyvolunty.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.wael.tyvolunty.*
import kotlinx.android.synthetic.main.activity_accueil.*
import kotlinx.android.synthetic.main.fragmentvolunteer.*
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.item_age.*
import kotlinx.android.synthetic.main.item_email.*
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.android.synthetic.main.item_info.*
import kotlinx.android.synthetic.main.toolbarprofile.*

class fragmentvolunteer:Fragment(R.layout.fragmentvolunteer) {

    private lateinit var mSharedPref: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var rootView: View = inflater.inflate(R.layout.fragmentvolunteer, container, false)

        return rootView

    }
    fun refresh(context: Context?)
    {
        context?.let {
            val fragementManager = (context as? AppCompatActivity)?.supportFragmentManager
            fragementManager?.let {
                val currentFragement = fragementManager.findFragmentById(R.id.container)
                currentFragement?.let {
                    val fragmentTransaction = fragementManager.beginTransaction()
                    fragmentTransaction.detach(it)
                    fragmentTransaction.attach(it)
                    fragmentTransaction.commit()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       // (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
       refresh(context)

        mSharedPref = requireContext().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);

        requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        if(mSharedPref.getString(PHOTO, "").toString()=="NO PICTURE")
        {
            profilePic22!!.setImageResource(R.drawable.avtar)
        }
        else
        {
            val filename2 = mSharedPref.getString(PHOTO, "").toString()
            Log.e("image",filename2)
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F$filename2?alt=media"
            Glide.with(context)
                .load(path)
                .into(profilePic22)
        }
        name.text = mSharedPref.getString(USERNAME, "").toString()
        email.text = mSharedPref.getString(EMAIL, "").toString()
        age.text = mSharedPref.getString(AGE, "").toString()

        logout.setOnClickListener{
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to logout ?")
            builder.setPositiveButton("Yes"){ dialogInterface, which ->
                requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
                val intent = Intent(context,Login::class.java)
                startActivity(intent)
            }
            builder.setNegativeButton("No"){dialogInterface, which ->
                dialogInterface.dismiss()
            }
            builder.create().show()

        }
        update.setOnClickListener{
            val intent = Intent(context,MainActivity::class.java)
            startActivity(intent)
        }





        super.onViewCreated(view, savedInstanceState)
    }

}