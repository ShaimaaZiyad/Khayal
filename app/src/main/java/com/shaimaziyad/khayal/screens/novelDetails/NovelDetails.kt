package com.shaimaziyad.khayal.screens.novelDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.NovelDetailsBinding

import com.shaimaziyad.khayal.utils.Constants
import com.shaimaziyad.khayal.utils.showMessage


class NovelDetails : Fragment() {

    private lateinit var binding: NovelDetailsBinding
    private var novel: NovelData? = null
    private val chapterController by lazy { ChapterController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = NovelDetailsBinding.inflate(layoutInflater)

        setData()
        setViews()


        return binding.root
    }

    private fun setData() {
        novel = try {
            arguments?.get(Constants.NOVEL_KEY) as NovelData
        } catch (ex: Exception) {
            null
        }
    }

    private fun setChapterAdapter() {

        // init data
        chapterController.setData(novel?.pdfs)

        /** on chapter click **/
        chapterController.clickListener = object : ChapterController.OnClickListener {
            override fun onChapterClicked(pdfUri: String) {
                val data = bundleOf(Constants.PDF_KEY to pdfUri)
                findNavController().navigate(R.id.action_novelDetails_to_viewPdf, data)

            }
        }
        binding.rvChapters.adapter = chapterController.adapter
    }


    private fun setViews() {
        binding.apply {

            novelData = novel
            lifecycleOwner = this@NovelDetails

            setChapterAdapter()
            setAppBar()

            // todo: add favorite feature

        }
    }

    private fun setAppBar() {
        binding.apply {

            /** button like **/
            btnLike.setOnClickListener {
                showMessage("favorite")
            }

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            /** button options **/
            btnOptions.setOnClickListener {
                // TODO: add some options
                showMessage("options")
            }

            /** button share **/
            btnShare.setOnClickListener {
                // todo add share feature
                showMessage("share")
            }

        }
    }


}