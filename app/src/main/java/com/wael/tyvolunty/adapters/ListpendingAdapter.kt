package com.wael.tyvolunty.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.wael.tyvolunty.AcceptDecline
import com.wael.tyvolunty.DetailCall
import com.wael.tyvolunty.DetailCallRecruiter
import com.wael.tyvolunty.R
import com.wael.tyvolunty.models.Calls
import com.wael.tyvolunty.models.VolunteerCall

import kotlinx.android.synthetic.main.listallfrag.view.*
import kotlinx.android.synthetic.main.listallfrag.view.taptores
import kotlinx.android.synthetic.main.listpending.view.*

class ListpendingAdapter (var calls:MutableList<VolunteerCall>):
    RecyclerView.Adapter<ListpendingAdapter.CallviewHolder>() {
    inner class CallviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallviewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.listpending, parent, false)
        return CallviewHolder(view)
    }

    override fun onBindViewHolder(holder: CallviewHolder, position: Int) {

        holder.itemView.apply {


                val path = "https://firebasestorage.googleapis.com/v0/b/tyvolunty.appspot.com/o/images%2F%E2%80%94Pngtree%E2%80%94volunteer%20cleaning%20up%20garbage%20concept_6689209.png?alt=media&token=4e085d63-bbd7-4780-9f45-669114c6ba07"
                Glide.with(this)
                    .load(path)

                    .into(resto_reserv_img)




            // listner
            users_reservation_desc.setText(calls[position].status)
            if ( calls[position].status =="pending"){
                reservationshow.setBackgroundColor(Color.YELLOW)
            }else if (calls[position].status =="accepted"){
                reservationshow.setBackgroundColor(Color.GREEN)
            }else{
                reservationshow.setBackgroundColor(Color.RED)
            }
            reservationshow.setOnClickListener {
                val activity = holder.itemView.context as Activity
                val intent = Intent(activity, AcceptDecline::class.java)
                intent.putExtra("id",calls[position]._id)
                intent.putExtra("idV",calls[position].idV)
                intent.putExtra("callId",calls[position].callId)
                intent.putExtra("status",calls[position].status)
                activity.startActivity(intent)

            }
        }
    }

    override fun getItemCount(): Int {
        return calls.size
    }

}
