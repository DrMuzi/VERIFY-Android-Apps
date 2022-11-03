package com.b21cap0133.verify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.b21cap0133.verify.MainActivity
import com.b21cap0133.verify.databinding.FragmentHoaxEduBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class HoaxEduFragment : Fragment() {

    private var _binding: FragmentHoaxEduBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHoaxEduBinding.inflate(inflater, container, false)
        val parent: MainActivity = activity as MainActivity
        parent.showUpButton()
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}