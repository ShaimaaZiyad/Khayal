package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.RegisterBinding

class Register : Fragment() {

    private lateinit var binding: RegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = RegisterBinding.inflate(layoutInflater)

        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {
            registerBtn.setOnClickListener {
                Toast.makeText(requireContext(),"enter to app", Toast.LENGTH_SHORT).show()
            }
            haveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_register_to_login)
            }
            /** button back **/
            backBtn.setOnClickListener{
//                requireActivity().onBackPressed()
                findNavController().navigateUp()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}