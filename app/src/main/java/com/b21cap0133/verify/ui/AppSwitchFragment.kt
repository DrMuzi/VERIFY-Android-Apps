package com.b21cap0133.verify.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.b21cap0133.verify.MainActivity
import com.b21cap0133.verify.R
import com.b21cap0133.verify.databinding.FragmentAppSwitchBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AppSwitchFragment : Fragment() {
    private var _binding: FragmentAppSwitchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppSwitchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLayout() {
        val parent: MainActivity = activity as MainActivity
        parent.showUpButton()
        var image: Int = R.drawable.logo_verify_big
        var text = "Pergi ke halaman verify"
        when (arguments?.getInt(MainScreenFragment.IDENTIFIER)){
            1 -> {
                image = R.drawable.turnbackhoax
                text = "Pergi ke web turnbackhoax"
                binding.button1.setOnClickListener {
                    val url = "https://turnbackhoax.id/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            2 -> {
                image = R.drawable.logo_fact_check
                text = "Pergi ke web cekfakta"
                binding.button1.setOnClickListener {
                    val url = "https://cekfakta.com/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            3 -> {
                image = R.drawable.logo_jalahoax
                text = "Pergi ke web JalaHoax"
                binding.button1.setOnClickListener {
                    val url = "https://data.jakarta.go.id/jalahoaks/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            4 -> {
                image = R.drawable.logo_jakarta_post
                text = "Pergi ke web The Jakarta Post"
                binding.button1.setOnClickListener {
                    val url = "https://www.thejakartapost.com/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            5 -> {
                image = R.drawable.logo_jaki
                text = "Pindah ke JaKi"
                binding.button1.setOnClickListener {
                    val url = "https://jaki.jakarta.go.id/"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        }
        Glide.with(this)
            .load(image)
            .apply(RequestOptions().override(250, 250))
            .into(binding.appIcon)
        binding.button1.text = text
    }
}