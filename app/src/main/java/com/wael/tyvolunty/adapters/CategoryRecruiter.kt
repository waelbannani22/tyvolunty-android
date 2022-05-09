package com.wael.tyvolunty.adapters


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.wael.tyvolunty.*
import com.wael.tyvolunty.fragment.listCalls
import com.wael.tyvolunty.models.Category






class CategoryRecruiter(private val categoryList: List<Category> ,private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<CategoryRecruiter.CategoryViewHolderR>(){

    private lateinit var mSharedPref: SharedPreferences
    class CategoryViewHolderR(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryImageView:ImageView= itemView.findViewById(R.id.imageVieweach)
        val categoryNameTv:TextView= itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolderR {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_category_item,parent,false )
        mSharedPref = view.context.getSharedPreferences(PREF_NAMER, MODE_PRIVATE)
        return  CategoryViewHolderR(view)
    }

    override fun onBindViewHolder(holder: CategoryRecruiter.CategoryViewHolderR, position: Int) {
        val category = categoryList[position]
        holder.categoryImageView.setImageResource(category.categoryImage)
        holder.categoryNameTv.text = category.categoryName

        holder.itemView.setOnClickListener {

            mSharedPref.edit().apply {
                putString(CATEGORYR,category.categoryName)



            }.apply()

            cellClickListener.onCellClickListener(category.categoryName)
            Log.e("cat",mSharedPref.getString(CATEGORYR,"").toString())

        }
    }

    override fun getItemCount(): Int {
        return  categoryList.size
    }



}