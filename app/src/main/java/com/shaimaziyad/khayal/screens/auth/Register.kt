package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.RegisterBinding

class Register : Fragment() {

    private lateinit var binding: RegisterBinding
    private lateinit var viewModel: AuthViewModel
    private var mName = ""
    private var mEmail = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = RegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {

            btnRegister.setOnClickListener {
                Toast.makeText(requireContext(),"enter to app", Toast.LENGTH_SHORT).show()
            }

            btnHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_register_to_login)
            }

            /** button back **/
            backBtn.setOnClickListener{
//                requireActivity().onBackPressed()
                findNavController().navigateUp()
            }

        }
    }


    private fun signUp(){
        binding.apply {
            mName = name.text?.trim().toString()
            mEmail = email.text?.trim().toString()




        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}