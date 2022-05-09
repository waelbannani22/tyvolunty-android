package com.wael.tyvolunty

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wael.tyvolunty.fragment.*
import kotlinx.android.synthetic.main.item_bulle.*

class AccueilRecruiter : AppCompatActivity() {
    private lateinit var mSharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil_recruiter)
        //bind
        val toolbar: Toolbar = findViewById(R.id.app_bar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        //setSupportActionBar(toolbar)
        mSharedPref = getSharedPreferences(PREF_NAMER, MODE_PRIVATE);
        //frag
        val volunteer_Fragment = fragmentvolunteer()
        val blank = BlankFragment()
        val home = HomeRecruiter()
        val listcall = listCalls()
        toolbar.visibility = View.VISIBLE
        DesignImage.visibility = View.VISIBLE
        setCurrentFragment(home)
        if(mSharedPref.getString(PHOTOR, "").toString()=="NO PICTURE")
        {
            ProfilImg!!.setImageResource(R.drawable.wae)
        }
        else
        {
            val filename2 = mSharedPref.getString(PHOTOR, "").toString()
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F2022_04_27_01_01_21?alt=media"
            Glide.with(this)
                .load(path)
                .into(ProfilImg)
        }
        bottomNavigationView.setItemIconTintList(null)
        bottomNavigationView.setOnItemSelectedListener() {

            when(it.itemId) {
                R.id.navigation_home-> {

                    setCurrentFragment(home)

                }
                R.id.profile-> {

                    setCurrentFragment(volunteer_Fragment)

                }

            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            addToBackStack("")
            commit()
        }
}