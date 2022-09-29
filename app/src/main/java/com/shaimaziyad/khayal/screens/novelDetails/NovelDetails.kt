package com.shaimaziyad.khayal.screens.novelDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.NovelDetailsBinding


class NovelDetails : Fragment() {

    private lateinit var binding: NovelDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = NovelDetailsBinding.inflate(layoutInflater)

        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {
            readBtn.setOnClickListener {
                findNavController().navigate(R.id.action_novelDetails_to_novelPdf)
            }
            favoriteBtn.setOnClickListener {
                Toast.makeText(requireContext(),"تمت الاضافة الى المفضلة", Toast.LENGTH_SHORT).show()
            }
            /** button back **/
            btnBack.setOnClickListener{
                requireActivity().onBackPressed()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}