package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.databinding.RegisterBinding
import com.shaimaziyad.khayal.utils.UserType
import com.shaimaziyad.khayal.utils.getCurrentTime
import com.shaimaziyad.khayal.utils.showMessage

class Register : Fragment() {

    private lateinit var binding: RegisterBinding
    private lateinit var viewModel: AuthViewModel
    private var mName = ""
    private var mEmail = ""
    private var mPassword = ""
    private var mConfPassword = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = RegisterBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        setViews()
        setObserves()

        return binding.root
    }

    private fun setViews() {
        binding.apply {

            authViewModel = viewModel

            /** button register **/
            btnSignUp.setOnClickListener {
                signUp()
            }

            /** button have account **/
            btnHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_register_to_login)
            }

            /** button back **/
            btnBack.setOnClickListener{
                findNavController().navigateUp()
            }


        }
    }



    private fun setObserves() {
        viewModel.apply {


            /** isRegister live data **/
            isRegister.observe(viewLifecycleOwner){ isRegister ->
                if (isRegister == true){
                    navigateToLogin()
                    showMessage(getString(R.string.message_verify_account))
                }
            }






        }
    }

    private fun navigateToLogin(){
        findNavController().navigate(R.id.action_register_to_login)
    }



    private fun signUp() {
        binding.apply {
            mName = name.text?.trim().toString()
            mEmail = email.text?.trim().toString()
            mPassword = password.text?.trim().toString()
            mConfPassword = confirmPassword.text?.trim().toString()

            if (isAllFieldsFilled()) { // all fields empty
                showMessage(getString(R.string.all_fields_empty_error))
            }else {

                // name
                if (mName.isEmpty()) {
                    name.error = getString(R.string.name_empty_error)
                    name.requestFocus()
                }else if (mName.length < 2){
                    name.error = getString(R.string.name_invalid_error)
                    name.requestFocus()
                }

                // email
                if (mEmail.isEmpty()) {
                    email.error = getString(R.string.email_empty_error)
                    email.requestFocus()
                }else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    email.error = getString(R.string.email_invalid_error)
                    email.requestFocus()
                }


                // password
                if (mPassword.isEmpty()) {
                    password.error = getString(R.string.password_empty_error)
                    password.requestFocus()
                }else if (mPassword.length < 6) {
                    password.error = getString(R.string.password_is_less_error)
                    password.requestFocus()
                }

                // conf password
                if (mConfPassword.isEmpty()) {
                    password.error = getString(R.string.password_empty_error)
                    password.requestFocus()
                }else if (mConfPassword != mPassword) {
                    password.error = getString(R.string.password_mismatch_error)
                    password.requestFocus()
                }

                else{
                    val user = User("",mEmail,mName,"",UserType.USER.name,mPassword)
                    viewModel.register(user)
                }


            }

        }
    }



    private fun isAllFieldsFilled(): Boolean {
        return mName.isEmpty() && mEmail.isEmpty() && mPassword.isEmpty() && mConfPassword.isEmpty()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}