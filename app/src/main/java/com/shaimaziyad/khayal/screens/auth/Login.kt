package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.LoginBinding

class Login : Fragment() {

    private lateinit var binding: LoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginBinding.inflate(layoutInflater)

        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {

            loginBtn.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_home)
//                Toast.makeText(requireContext(),"تم الدخول بنجاح",Toast.LENGTH_SHORT).show()
            }
            noAccountTv.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }
            forgotTv.setOnClickListener {
                showBottomSheet()
            }
            googleSignInBtn.setOnClickListener {
                Toast.makeText(requireContext(),"تم الدخول بنجاح",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showBottomSheet() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_forgot_sheet)
        val submitBtn = dialog.findViewById<Button>(R.id.submitBtn)

        submitBtn?.setOnClickListener {
            // resetPassword()
            Toast.makeText(requireContext(), " تم الارسال" , Toast.LENGTH_SHORT).show()
        }

        dialog.show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}