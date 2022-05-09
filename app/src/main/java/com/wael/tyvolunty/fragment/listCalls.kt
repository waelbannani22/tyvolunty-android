package com.wael.tyvolunty.fragment

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.wael.tyvolunty.*
import com.wael.tyvolunty.adapters.CallsAdapter
import com.wael.tyvolunty.utilis.ApiInterface
import kotlinx.android.synthetic.main.list_calls_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class listCalls : Fragment(R.layout.list_calls_fragment) {



    private lateinit var mSharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_calls_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        mSharedPref = requireContext().getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);


        val apiInterface = ApiInterface.create()

        val map: HashMap<String, String> = HashMap()

        map["category"] = mSharedPref.getString(CATEGORY,"").toString()
        CoroutineScope(Dispatchers.IO).launch {

            try {
                val anyMutableList1 = mutableListOf("bezkoder.com", "2019")
                val response = apiInterface.fetchbycategory(map)
                Log.e("response", response.toString())
                withContext(Dispatchers.Main){
                    val calls = response.body()
                    if ( response.code()== 200){
                        val adapter = response.body()?.let { CallsAdapter(it) }
                        rv_resto.adapter = adapter
                        rv_resto.apply {
                            layoutManager = GridLayoutManager(view.context, 2)
                        }
                    }else{
                        Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                    }
                }

            }catch (e: Exception){
                Log.e("exeption",e.toString())

            }


        }


        super.onViewCreated(view, savedInstanceState)
    }
    fun refresh(context: Context?)
    {
        context?.let {
            val fragementManager = (context as? AppCompatActivity)?.supportFragmentManager
            fragementManager?.let {
                val currentFragement = fragementManager.findFragmentById(R.id.fragmentcall)
                currentFragement?.let {
                    val fragmentTransaction = fragementManager.beginTransaction()
                    fragmentTransaction.detach(it)
                    fragmentTransaction.attach(it)
                    fragmentTransaction.commit()
                }
            }
        }
    }

}