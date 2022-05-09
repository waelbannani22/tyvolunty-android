package com.wael.tyvolunty.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wael.tyvolunty.DetailCall
import com.wael.tyvolunty.R
import com.wael.tyvolunty.models.Calls
import kotlinx.android.synthetic.main.listallfrag.view.*

class CallsAdapter (var calls:MutableList<Calls>):
    RecyclerView.Adapter<CallsAdapter.CallviewHolder>() {
    inner class CallviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallviewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.listallfrag, parent, false)
        return CallviewHolder(view)
    }

    override fun onBindViewHolder(holder: CallviewHolder, position: Int) {
        var filename : String
        holder.itemView.apply {
            filename = calls[position].photo
            val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F2022_04_27_01_01_21?alt=media&token=73554f53-d1cd-4839-8543-f555c21406f1"
            Glide.with(this)
                .load(path)
                .into(resto_img)
            // listner
            resto_name.setText(calls[position].name)
            taptores.setOnClickListener {
                val activity = holder.itemView.context as Activity
                val intent = Intent(activity, DetailCall::class.java)
                intent.putExtra("id",calls[position]._id)
                intent.putExtra("name",calls[position].name)
                intent.putExtra("picture",calls[position].photo)
                intent.putExtra("description",calls[position].description)


                activity.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return calls.size
    }

}
