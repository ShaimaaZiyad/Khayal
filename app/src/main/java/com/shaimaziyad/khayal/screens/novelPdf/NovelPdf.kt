package com.shaimaziyad.khayal.screens.novelPdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shaimaziyad.khayal.databinding.NovelPdfBinding


class NovelPdf : Fragment() {

    private lateinit var binding: NovelPdfBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = NovelPdfBinding.inflate(layoutInflater)

        setViews()

        return binding.root
    }

    private fun setViews() {
        binding.apply {
            logoutBtn.setOnClickListener {
                requireActivity().finish()
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