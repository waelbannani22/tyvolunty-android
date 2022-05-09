package com.wael.tyvolunty.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wael.tyvolunty.*
import com.wael.tyvolunty.adapters.CategoryAdapter
import com.wael.tyvolunty.adapters.CategoryRecruiter
import com.wael.tyvolunty.models.Category
import kotlinx.android.synthetic.main.activity_accueil.*
import kotlinx.android.synthetic.main.each_category_item.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_bulle.*
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.android.synthetic.main.toolbarprofile.*

class HomeRecruiter : Fragment(), CellClickListener {

    // private lateinit var recyclerView: RecyclerView
    private lateinit var categoryList: ArrayList<Category>
    private lateinit var categoryAdapter: CategoryRecruiter
    private lateinit var mSharedPref: SharedPreferences
    private lateinit var i: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_recruiter, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        init()



        super.onViewCreated(view, savedInstanceState)
    }
    private fun init(){
        i = "c"
        val io= CellClickListener(){onCellClickListener(i)}
        // RecyclerView12.setHasFixedSize(true)
        homefragment.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL,false)
        categoryList = ArrayList()
        add()

        categoryAdapter = CategoryRecruiter(categoryList,io)
        homefragment.adapter = categoryAdapter

    }
    private fun add(){

        categoryList.add(Category(R.drawable.animal,"ANIMAL"))
        categoryList.add(Category(R.drawable.senior,"SENIOR"))
        categoryList.add(Category(R.drawable.children,"CHILDREN"))
        categoryList.add(Category(R.drawable.community,"COMMUNITY"))
        categoryList.add(Category(R.drawable.environment,"ENVIRONMENT"))
        categoryList.add(Category(R.drawable.health,"HEALTH"))
        categoryList.add(Category(R.drawable.homeless,"HOMELESS"))
        categoryList.add(Category(R.drawable.disabilty,"DISABILITY"))
        categoryList.add(Category(R.drawable.eduction,"EDUCATION"))

        mSharedPref = requireContext().getSharedPreferences(PREF_NAMER, AppCompatActivity.MODE_PRIVATE);



    }





    override fun onCellClickListener(i:String) {

        val intent = Intent(context, CallsRecruiter::class.java)
        startActivity(intent)



    }
/*
    override fun onCellClickListener(ik: String) {
        Toast.makeText(
            context,
            ik,
            Toast.LENGTH_SHORT
        ).show()
    }
    */


}