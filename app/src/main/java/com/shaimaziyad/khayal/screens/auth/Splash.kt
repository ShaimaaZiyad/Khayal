package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.SplashBinding


class Splash : Fragment() {

    private lateinit var binding: SplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SplashBinding.inflate(layoutInflater)

        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splash_to_login)
        } , 3000)

        return binding.root
    }
}