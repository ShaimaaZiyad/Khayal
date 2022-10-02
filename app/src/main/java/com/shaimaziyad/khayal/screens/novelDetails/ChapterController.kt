package com.shaimaziyad.khayal.screens.novelDetails

import com.airbnb.epoxy.TypedEpoxyController
import com.shaimaziyad.khayal.chapter


class ChapterController(): TypedEpoxyController<List<String>>() {

    lateinit var clickListener: OnClickListener

    override fun buildModels(list: List<String>?) {

        list?.forEachIndexed { index , uri ->
            val chapterNumber = "Chapter ${index + 1}"
            chapter {
                id(index)
                chapterNumber(chapterNumber)
                onChapterClick { v-> // onChapter Click
                    clickListener.onChapterClicked(uri)
                }
            }


        }

    }


    interface OnClickListener {
        fun onChapterClicked(pdfUri: String)
    }


}