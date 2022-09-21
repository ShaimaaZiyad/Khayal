package com.shaimaziyad.khayal.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.ProfileBinding


class Profile : Fragment() {

    private lateinit var binding: ProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = ProfileBinding.inflate(layoutInflater)

        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {
            profileEditBtn.setOnClickListener {
                showBottomSheet()
            }
            /** button back **/
            backBtn.setOnClickListener{
                requireActivity().onBackPressed()
            }
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_edit_profile_sheet)
        val updateBtn = dialog.findViewById<Button>(R.id.updateBtn)

        updateBtn?.setOnClickListener {
            // updateProfile()
            Toast.makeText(requireContext(), " تم التحديث" , Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}