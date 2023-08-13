package com.allrecipes.recipes5.csm.topsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.allrecipes.recipes5.R
import com.bumptech.glide.Glide

class ChanceOverDialog : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.coins__dialog, container, false)
        rootView.apply {
            Glide.with(this)
                .load(ContextCompat.getDrawable(context, R.drawable.ad_image))
                .into(findViewById<ImageView>(R.id.topImage))
            findViewById<LinearLayout>(R.id.coi).visibility = INVISIBLE
            findViewById<TextView>(R.id.txt_1).text = "Congratulation !"
            findViewById<TextView>(R.id.txt_2).text = "You got a extra spin for watching video"
            findViewById<LinearLayout>(R.id.ok).setOnClickListener {
                dismiss()
            }
        }
        return rootView
    }
}