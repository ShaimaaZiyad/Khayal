package com.shaimaziyad.khayal.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.databinding.AddCategoryBinding

class AddCategory : Fragment() {

    private lateinit var binding: AddCategoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AddCategoryBinding.inflate(layoutInflater)

        setViews()


        return binding.root
    }

    private fun setViews() {
        binding.apply {

            submitBtn.setOnClickListener {
                Toast.makeText(requireContext(),"send data success", Toast.LENGTH_SHORT).show()
            }
            /** button back **/
            backBtn.setOnClickListener{
//                requireActivity().onBackPressed()
                findNavController().navigateUp()
            }


        }
    }



}