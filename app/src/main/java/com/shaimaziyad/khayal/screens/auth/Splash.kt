package com.shaimaziyad.khayal.screens.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.SplashBinding
import com.shaimaziyad.khayal.utils.showMessage


class Splash : Fragment() {

    private lateinit var binding: SplashBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SplashBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        Handler(Looper.myLooper()!!).postDelayed({

            setObserves()

            findNavController().navigate(R.id.action_splash_to_login)
        }, 3000)

        return binding.root
    }

    private fun setObserves() {
        viewModel.apply {


            /** isLogged live data **/
            isLogged.observe(viewLifecycleOwner){isLogged ->
                if (isLogged == true) {
                   findNavController().navigate(R.id.action_splash_to_home)
                }
                else{
                    findNavController().navigate(R.id.action_splash_to_login)
                }
            }
        }
    }


}