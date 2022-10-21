package com.shaimaziyad.khayal.sheets

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.databinding.FilterNovelSheetBinding
import com.shaimaziyad.khayal.databinding.FilterUserSheetBinding
import com.shaimaziyad.khayal.utils.*

class FilterUserSheet(private val context: Context,
                      private val binding: FilterUserSheetBinding,
                      private val fragment: Fragment) {

    lateinit var filterStatus: FilterStatus
    lateinit var users: List<User>

    private val sheet = BottomSheetBehavior.from(binding.sheet)


    fun hideSheet() {
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
    }

    fun showSheet() {
        binding.sheet.show()
        sheet.isDraggable = false
        sheet.state = BottomSheetBehavior.STATE_EXPANDED

        var country: String? = null



        /** button clear Filter **/
        binding.btnClearFilter.setOnClickListener {
            country = null
            clearFilter()
        }

        binding.selectCountry.setOnClickListener {
            country =  binding.selectCountry.selectedCountryName
            updateBtnApplyInfo(filter(country).size)
        }

        /** button apply **/
        binding.btnApply.setOnClickListener {
            val filtered = filter(country)
            filterStatus.onFilter(filtered)
            hideSheet()
        }

        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

    }




    private fun clearFilter() {
        binding.btnApply.text = context.getString(R.string.apply)

        filterStatus.clearFilter()
    }


    private fun updateBtnApplyInfo(value: Int){
        binding.btnApply.text = context.getString(R.string.results,value.toString())
    }


    fun filter(country: String?): List<User> {
        var filtered = users
        if (country != null) {
//            filtered = users.filter { it == country }
        }
        return filtered
    }


    interface FilterStatus {
        fun onFilter(filtered: List<User>)
        fun clearFilter()
    }



}