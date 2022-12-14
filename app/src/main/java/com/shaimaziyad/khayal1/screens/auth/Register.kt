package com.shaimaziyad.khayal1.screens.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal1.R
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.databinding.RegisterBinding
import com.shaimaziyad.khayal1.utils.UserType
import org.koin.android.viewmodel.ext.android.sharedViewModel

class Register : Fragment() {

    private lateinit var binding: RegisterBinding
    private val viewModel by sharedViewModel<AuthViewModel>()
    private var mName = ""
    private var mEmail = ""
    private var mCountry = ""
    private var mPassword = ""
    private var mConfPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = RegisterBinding.inflate(layoutInflater)


        setViews()
        setObserves()

        return binding.root
    }

    private fun setViews() {
        binding.apply {

            authViewModel = viewModel
            lifecycleOwner = this@Register

            /** button register **/
            btnSignUp.setOnClickListener {

                mName = name.text?.trim().toString()
                mEmail = email.text?.trim().toString()
                mCountry = country.text?.trim().toString()
                mPassword = password.text?.trim().toString()
                mConfPassword = confirmPassword.text?.trim().toString()


                // name
                if (mName.isEmpty()) {
                    name.error = getString(R.string.name_empty_error)
                    name.requestFocus()
                    return@setOnClickListener

                }
                if (mName.length < 2) {
                    name.error = getString(R.string.name_invalid_error)
                    name.requestFocus()
                    return@setOnClickListener
                }

                // email
                if (mEmail.isEmpty()) {
                    email.error = getString(R.string.email_empty_error)
                    email.requestFocus()
                    return@setOnClickListener
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    email.error = getString(R.string.email_invalid_error)
                    email.requestFocus()
                    return@setOnClickListener
                }
                if (mCountry.isEmpty()) {
                    country.error = getString(R.string.country_empty_error)
                    country.requestFocus()
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

                // conf password
                if (mConfPassword.isEmpty()) {
                    confirmPassword.error = getString(R.string.password_empty_error)
                    confirmPassword.requestFocus()
                    return@setOnClickListener
                }
                if (mConfPassword != mPassword) {
                    confirmPassword.error = getString(R.string.password_mismatch_error)
                    confirmPassword.requestFocus()
                    return@setOnClickListener
                } else {
                    val user = User("", mEmail, mName, "", UserType.USER.name, mPassword, mCountry)
                    viewModel.register(user)

                }

            }

            /** button have account **/
            btnHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_register_to_login)
                viewModel.resetStatus()
            }

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }


        }
    }


    private fun setObserves() {
        viewModel.apply {


            /** isRegister live data **/
            isRegister.observe(viewLifecycleOwner) { isRegister ->
                if (isRegister == true) {
                    navigateToLogin()
                }
            }


        }
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_register_to_login)
    }


}