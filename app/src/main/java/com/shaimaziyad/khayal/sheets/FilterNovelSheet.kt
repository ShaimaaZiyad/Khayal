package com.shaimaziyad.khayal.sheets

import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.databinding.FilterNovelSheetBinding
import com.shaimaziyad.khayal.utils.*

class FilterNovelSheet(private val binding: FilterNovelSheetBinding,
                       private val fragment: Fragment) {

    lateinit var filterStatus: FilterStatus
    lateinit var novels: List<Novel>
    private val novelFilter = NovelFilter(fragment.requireContext())
    private val sheet = BottomSheetBehavior.from(binding.sheet)

    private var category: String? = null
    private var type: String? = null

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
           filter(category, type)
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


    fun clearFilter() {
        category = null
        type = null
        binding.btnApply.text = fragment.requireContext().getString(R.string.apply)
        binding.btnSelectCategory.setText(R.string.category_title)
        binding.btnSelectType.setText(R.string.novel_type)
        setFilters()
        filterStatus.clearFilter()
    }


    private fun updateBtnApplyInfo(value: Int){
        binding.btnApply.text = fragment.requireContext().getString(R.string.results,value.toString())
    }


    // filter = novel category || novel type
    fun filter(category: String?, type: String?): List<Novel> {
        var filtered = novels
        if (category != null) {
            filtered = novels.filter { it.category == category }
            filterStatus.onFilter(filtered)
        }
        if (type != null){
            filtered = novels.filter { it.type == type }
            filterStatus.onFilter(filtered)
        }
        if (category == null && type == null){
            clearFilter()
        }
        return filtered
    }


    interface FilterStatus {
        fun onFilter(filtered: List<Novel>)
        fun clearFilter()
        fun onSheetOpen(){}
        fun onSheetClose(){}
    }



}