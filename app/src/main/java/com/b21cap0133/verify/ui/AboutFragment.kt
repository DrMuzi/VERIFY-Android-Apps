package com.b21cap0133.verify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.b21cap0133.verify.MainActivity
import com.b21cap0133.verify.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parent: MainActivity = activity as MainActivity
        parent.hideUpButton()
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

}