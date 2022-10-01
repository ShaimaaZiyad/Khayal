package com.shaimaziyad.khayal.screens.profile

import android.os.Bundle
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
import com.shaimaziyad.khayal.databinding.ProfileBinding
import com.shaimaziyad.khayal.utils.showMessage


class Profile : Fragment() {

    private lateinit var binding: ProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        binding = ProfileBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]



        setViews()
        setObserves()

        return binding.root
    }

    private fun setObserves() {
        viewModel.apply {

            /** isLoggedOut live data **/
            isLoggedOut.observe(viewLifecycleOwner){
                if (it == true){
                    findNavController().navigate(R.id.action_profile_to_login)
                }
            }

        }
    }

    private fun setViews() {
        binding.apply {

            profileViewModel = viewModel
            lifecycleOwner = this@Profile


            /** button edit profile **/
            profileEditBtn.setOnClickListener {
                showBottomSheet()
            }
            /** button back **/
            btnBack.setOnClickListener{
                findNavController().navigateUp()
            }

            /** button sign Out **/
            btnSignOut.setOnClickListener {
                viewModel.signOut()
            }

        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_edit_profile_sheet)
        val updateBtn = dialog.findViewById<Button>(R.id.updateBtn)

        updateBtn?.setOnClickListener {
            // updateProfile()
           showMessage("update")
        }

        dialog.show()
    }



}