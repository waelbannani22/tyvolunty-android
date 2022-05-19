package com.wael.tyvolunty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ramotion.foldingcell.FoldingCell

import kotlinx.android.synthetic.main.diagonnal.*

class choose : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_choose)
        setContentView(R.layout.diagonnal)
        // get our folding cell


        folding_cell.setOnClickListener{
            folding_cell.toggle(false)

        }
        folding_cell1.setOnClickListener {
            folding_cell1.toggle(false)
        }






      recruiter!!.setOnClickListener {
            val mainIntent = Intent(this, SigninRecruiter::class.java)
            startActivity(mainIntent)
             finish()

        }
        volunteer.setOnClickListener {
            val mainIntent = Intent(this, Login::class.java)
            startActivity(mainIntent)
             finish()
        }


    }
}