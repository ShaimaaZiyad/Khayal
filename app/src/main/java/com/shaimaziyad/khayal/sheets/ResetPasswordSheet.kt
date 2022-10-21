package com.shaimaziyad.khayal.sheets

import android.util.Patterns
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.databinding.ResetPasswordSheetBinding
import com.shaimaziyad.khayal.utils.hide
import com.shaimaziyad.khayal.utils.hideKeyboard
import com.shaimaziyad.khayal.utils.show

class ResetPasswordSheet(
    private val binding: ResetPasswordSheetBinding,
    private val fragment: Fragment) {

    lateinit var resetPassStatus: ResetPasswordStatus

    private val sheet = BottomSheetBehavior.from(binding.sheet)


    fun hideSheet() {
        sheet.isDraggable = false
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
    }

    fun showSheet() {
        binding.sheet.show()
        sheet.state = BottomSheetBehavior.STATE_EXPANDED


        var email = ""


        /** button apply **/
        binding.btnSend.setOnClickListener {
            email = binding.email.text?.trim().toString()

            if (email.isEmpty()) {
                binding.email.error = fragment.getString(R.string.email_empty_error)
                binding.email.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.email.error = fragment.getString(R.string.email_invalid_error)
                binding.email.requestFocus()
                return@setOnClickListener
            }
            resetPassStatus.onSend(email)

        }


        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

    }


    interface ResetPasswordStatus{
        fun onSend(email: String)
    }

}