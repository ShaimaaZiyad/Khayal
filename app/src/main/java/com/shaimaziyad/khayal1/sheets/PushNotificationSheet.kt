package com.shaimaziyad.khayal1.sheets

import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal1.R
import com.shaimaziyad.khayal1.data.Notification
import com.shaimaziyad.khayal1.databinding.PushNotificationSheetBinding
import com.shaimaziyad.khayal1.utils.*
import com.shaimaziyad.khayal1.utils.getNovelId

class PushNotificationSheet(
    private val binding: PushNotificationSheetBinding,
    private val fragment: Fragment
) {

    lateinit var notifyStatus: NotifyStatus
    private val context = fragment.requireContext()
    var userId: String? = null

    private val sheet = BottomSheetBehavior.from(binding.sheet)


    fun hideSheet() {
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
    }

    fun showSheet() {
        resetViews()
        binding.sheet.show()
        sheet.isDraggable = false
        sheet.state = BottomSheetBehavior.STATE_EXPANDED

        if (userId != null) {
            binding.notifyInfo.text = context.getString(R.string.info_notify_for_user)
        } else {
            binding.notifyInfo.text = context.getString(R.string.info_notify_for_all_user)
        }


        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

        /** button send **/
        binding.btnSend.setOnClickListener {
            val title = binding.title.text?.trim().toString()
            val description = binding.description.text?.trim().toString()
            val externalLink = binding.exLink.text?.trim().toString()
            val internalLink = binding.inLink.text.trim().toString()
            val notification =
                Notification(getNovelId(), title, description, "", externalLink, internalLink, null)
            if (userId != null) { // push notification to specific user
                notification.targetUser = userId

            }

            if (title.isEmpty()) {
                binding.title.error = context.getString(R.string.error_require_title)
                binding.title.requestFocus()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.description.error = context.getString(R.string.error_require_description)
                binding.description.requestFocus()
                return@setOnClickListener
            }

            if (binding.external.isChecked) {
                if (externalLink.isEmpty()) {
                    binding.exLink.error = context.getString(R.string.error_require_external_link)
                    binding.exLink.requestFocus()
                    return@setOnClickListener
                } else {
                    notifyStatus.onSend(notification)
                }
            }

            if (binding.internal.isChecked) {
                if (internalLink.isEmpty()) {
                    fragment.showMessage(context.getString(R.string.error_require_internal_link))
                } else {
                    notifyStatus.onSend(notification)
                }
            }


            if (!binding.external.isChecked && !binding.internal.isChecked) {
                notifyStatus.onSend(notification)
            }


        }

        /** radio buttons **/
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.external -> {
                    binding.externalLinkField.show()
                    binding.internalLinkField.hide()
                }
                R.id.internal -> {
                    binding.externalLinkField.hide()
                    binding.internalLinkField.show()
                }
                R.id.nothing -> {
                    binding.externalLinkField.hide()
                    binding.internalLinkField.hide()
                }
            }
        }


    }

    private fun resetViews() {
        binding.apply {
            title.setText("")
            description.setText("")
            exLink.setText("")
        }
    }

    interface NotifyStatus {
        fun onSend(notify: Notification)
    }


}