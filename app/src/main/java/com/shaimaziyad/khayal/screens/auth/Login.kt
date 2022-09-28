package com.shaimaziyad.khayal.screens.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.LoginBinding
import com.shaimaziyad.khayal.utils.showMessage

class Login : Fragment() {

    private lateinit var binding: LoginBinding
    private lateinit var viewModel: AuthViewModel

    private var mEmail = ""
    private var mPassword = ""
    private var mIsRememberMe = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        setViews()
        setObserves()

        return binding.root
    }

    private fun setObserves() {
        viewModel.apply {


            /** isLogged live data **/
            isLogged.observe(viewLifecycleOwner){isLogged ->
                if (isLogged == true) {
                    showMessage("Login Success")
//                    findNavController().navigate(R.id.action_login_to_home)
                }
            }
        }
    }

    private fun setViews() {
        binding.apply {

            authViewModel = viewModel
            lifecycleOwner = this@Login

            /** button login **/
            btnLogin.setOnClickListener {
                mEmail = email.text?.trim().toString()
                mPassword = password.text?.trim().toString()
                mIsRememberMe = btnSwitchRem.isChecked

                // email
                if (mEmail.isEmpty()) {
                    email.error = getString(R.string.email_empty_error)
                    email.requestFocus()
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
                    email.error = getString(R.string.email_invalid_error)
                    email.requestFocus()
                    return@setOnClickListener
                }
                // password
                if (mPassword.isEmpty()) {
                    password.error = getString(R.string.password_empty_error)
                    password.requestFocus()
                    return@setOnClickListener
                }
                if (mPassword.length < 6) {
                    password.error = getString(R.string.password_is_less_error)
                    password.requestFocus()
                    return@setOnClickListener
                }
                else {
                    viewModel.login(mEmail,mPassword,mIsRememberMe)
                }

            }

            /** button sign up **/
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }

            /** button forgot password **/
            btnForgotPassword.setOnClickListener {
                showBottomSheet()
            }

            /** button login by google **/
            btnLoginByGoogle.setOnClickListener {
                loginByGoogle()
            }
        }
    }


    private fun showBottomSheet() {

        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_forgot_sheet)
        val submitBtn = dialog.findViewById<Button>(R.id.submitBtn)

        submitBtn?.setOnClickListener {
            // resetPassword()

        }

        dialog.show()

    }


    private fun loginByGoogle(){
        showMessage("Login by google")
        // todo: add functionality for login by google
    }



    private fun isAllFieldsFilled(): Boolean {
        return  mEmail.isEmpty() && mPassword.isEmpty()
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}