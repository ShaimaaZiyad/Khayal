package com.shaimaziyad.khayal.sheets

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.FilterNovelSheetBinding
import com.shaimaziyad.khayal.utils.*

class FilterNovelSheet(private val context: Context,
                       private val binding: FilterNovelSheetBinding,
                       private val fragment: Fragment) {

    lateinit var filterStatus: FilterStatus
    lateinit var novels: List<NovelData>
    private val novelFilter = NovelFilter(context)
    private val sheet = BottomSheetBehavior.from(binding.sheet)


    fun hideSheet() {
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
        filterStatus.onSheetClose()
    }

    fun showSheet() {
        filterStatus.onSheetOpen()
        sheet.isDraggable = false
        binding.sheet.show()
        sheet.state = BottomSheetBehavior.STATE_EXPANDED

        var category: String? = null
        var type: String? = null

        setFilters()

        /** select category **/
        binding.btnSelectCategory.setOnItemClickListener { adapterView, view, index, l ->
            category = index.toString()
            updateBtnApplyInfo(filter(category, type).size)
        }

        /** select type **/
        binding.btnSelectType.setOnItemClickListener { adapterView, view, index, l ->
            type = index.toString()
            updateBtnApplyInfo(filter(category, type).size)
        }

        /** button clear Filter **/
        binding.btnClearFilter.setOnClickListener {
            category = null
            type = null
            clearFilter()
        }

        /** button apply **/
        binding.btnApply.setOnClickListener {
            val filtered = filter(category, type)
            filterStatus.onFilter(filtered)
            hideSheet()
        }

        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

    }


    private fun setFilters() {
        binding.sheet.apply {
            setListToAutoComplete(context,novelFilter.novelCategories,binding.btnSelectCategory) // set category list
            setListToAutoComplete(context,novelFilter.novelType,binding.btnSelectType) // set novel list
        }
    }


    private fun clearFilter() {
        binding.btnApply.text = context.getString(R.string.apply)
        binding.btnSelectCategory.setText(R.string.category_title)
        binding.btnSelectType.setText(R.string.novel_type)
        setFilters()
        filterStatus.clearFilter()
    }


    private fun updateBtnApplyInfo(value: Int){
        binding.btnApply.text = context.getString(R.string.results,value.toString())
    }


    // filter = novel category || novel type
    fun filter(category: String?, type: String?): List<NovelData> {
        var filtered = novels
        if (category != null) {
            filtered = novels.filter { it.category == category }
        }
        if (type != null){
            filtered = novels.filter { it.type == type }
        }
        return filtered
    }


    interface FilterStatus {
        fun onFilter(filtered: List<NovelData>)
        fun clearFilter()
        fun onSheetOpen()
        fun onSheetClose()
    }



}