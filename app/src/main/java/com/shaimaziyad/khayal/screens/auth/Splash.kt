package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.SplashBinding
import com.shaimaziyad.khayal.utils.SharePrefManager
import org.koin.android.viewmodel.ext.android.sharedViewModel


class Splash : Fragment() {

    private lateinit var binding: SplashBinding
    private val viewModel by sharedViewModel<AuthViewModel>()
    private lateinit var sharePrefManager: SharePrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SplashBinding.inflate(layoutInflater)
        sharePrefManager = SharePrefManager(requireContext())


        Handler(Looper.myLooper()!!).postDelayed({
//            findNavController().navigate(R.id.action_splash_to_Login)
            isLogged()

        }, 3000)




        return binding.root
    }


    private fun isLogged() {
        val isUserLogged = viewModel.userRepo.isLogged
        if (isUserLogged) {
            findNavController().navigate(R.id.action_splash_to_home)
        } else {
            findNavController().navigate(R.id.action_splash_to_Login)
        }
    }


}