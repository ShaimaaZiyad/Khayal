package com.shaimaziyad.khayal.screens.novelDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.NovelDetailsBinding
import com.shaimaziyad.khayal.utils.Constants
import com.shaimaziyad.khayal.utils.showMessage


class NovelDetails : Fragment() {

    private lateinit var binding: NovelDetailsBinding
    private var novel: NovelData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        binding = NovelDetailsBinding.inflate(layoutInflater)

        setData()
        setViews()



        return binding.root
    }

    private fun setData() {
        novel = try { arguments?.get(Constants.NOVEL_KEY) as NovelData }
        catch (ex: Exception) { null }
    }

    private fun setViews() {
        binding.apply {

            novelData = novel
            lifecycleOwner = this@NovelDetails


            setAppBar()

            /** button read **/
            btnRead.setOnClickListener {
                findNavController().navigate(R.id.action_novelDetails_to_novelPdf)
            }

        }
    }

    private fun setAppBar() {
        binding.apply {

            /** button like **/
            btnLike.setOnClickListener {
                showMessage("favorite")
            }

            /** button back **/
            btnBack.setOnClickListener{
                findNavController().navigateUp()
            }

            /** button options **/
            btnOptions.setOnClickListener {
                showMessage("options")
            }

            /** button share **/
            btnShare.setOnClickListener {
                showMessage("share")
            }

        }
    }


}