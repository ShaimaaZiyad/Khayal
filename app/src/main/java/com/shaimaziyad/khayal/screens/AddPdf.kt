package com.shaimaziyad.khayal.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shaimaziyad.khayal.databinding.AddPdfBinding


class AddPdf : Fragment() {

    private lateinit var binding : AddPdfBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = AddPdfBinding.inflate(layoutInflater)

        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {
            submitBtn.setOnClickListener {
                Toast.makeText(requireContext(),"send data success", Toast.LENGTH_SHORT).show()
            }
            attachPdfBtn.setOnClickListener {
                Toast.makeText(requireContext(),"send data success", Toast.LENGTH_SHORT).show()
            }

            /** button back **/
            backBtn.setOnClickListener{
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}