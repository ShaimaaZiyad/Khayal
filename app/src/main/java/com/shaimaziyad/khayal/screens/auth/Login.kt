package com.shaimaziyad.khayal.screens.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.LoginBinding
import com.shaimaziyad.khayal.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal.sheets.ResetPasswordSheet
import com.shaimaziyad.khayal.utils.DataStatus
import org.koin.android.viewmodel.ext.android.sharedViewModel

class Login : Fragment() {

    private lateinit var binding: LoginBinding

    private val viewModel by sharedViewModel<AuthViewModel>()
    private val profileViewModel by sharedViewModel<ProfileViewModel>()

    private lateinit var googleSignInClient: GoogleSignInClient

    private companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var resetPassSheet: ResetPasswordSheet

    private var mEmail = ""
    private var mPassword = ""
    private var mIsRememberMe = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginBinding.inflate(layoutInflater)
        resetPassSheet = ResetPasswordSheet(binding.resetSheet, this)


        // Configure Google Sign In
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)


        setViews()
        setObserves()

        return binding.root
    }

    private fun setObserves() {
        viewModel.apply {


            /** isLogged live data **/
            isLogged.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    profileViewModel.refreshUser(user)
                    findNavController().navigate(R.id.action_login_to_home)
                    viewModel.resetStatus()
                }
            }


            /** reset password **/
            resetPasswordStatus.observe(viewLifecycleOwner) { status ->
                if (status != null) {
                    when (status) {
                        DataStatus.LOADING -> {

                        }
                        DataStatus.SUCCESS -> {
                            resetPassSheet.hideSheet()
                        }
                        DataStatus.ERROR -> {

                        }
                    }
                }
            }


        }
    }


    private fun setViews() {
        binding.apply {

            authViewModel = viewModel
            lifecycleOwner = this@Login


            resetPassSheetStatus()

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
                if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
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
                } else {
                    viewModel.login(mEmail, mPassword, mIsRememberMe, requireContext())
                }

            }

            /** button sign up **/
            btnSignup.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }

            /** button forgot password **/
            btnForgotPassword.setOnClickListener {
                resetPassSheet.showSheet()
            }

            /** button login by google **/
            btnLoginByGoogle.setOnClickListener {
                loginByGoogle()
            }


        }
    }


    private fun resetPassSheetStatus() {
        resetPassSheet.resetPassStatus = object : ResetPasswordSheet.ResetPasswordStatus {
            override fun onSend(email: String) {
                viewModel.resetPassword(email)
            }
        }
    }

    private fun loginByGoogle() {

        // begin google sign in
        Log.d(TAG, "onCreate : begin Google SignIn")
        val intent = googleSignInClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult : Google SignIn intent result")

            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = accountTask.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                // Google Sign In failed, update UI appropriately
                Log.d(TAG, "Google sign in failed : ${e.message}")
            }

        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {

        Log.d(TAG, "firebaseAuthWithGoogleAccount : begin firebase auth with google account")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                // login success

                Log.d(TAG, "firebaseAuthWithGoogleAccount : LoggedIn")

                // get loggedIn user
                val firebaseUser = firebaseAuth.currentUser

                // get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount : Uid : $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount : Email : $email")

                // check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {

                    // user is new - account created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount : Account created ... : \n$email")
                    Toast.makeText(
                        context,
                        getString(R.string.the_account_has_been_created),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    // existing user - loggedIn
                    Log.d(TAG, "firebaseAuthWithGoogleAccount : Existing user ... : \n$email")
                    Toast.makeText(context, getString(R.string.login_succeeded), Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }


    }
}