package com.shaimaziyad.khayal.sheets


import android.app.AlertDialog
import android.widget.EditText
import androidx.core.view.marginBottom
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.databinding.HomeOptionSheetBinding
import com.shaimaziyad.khayal.screens.home.Home
import com.shaimaziyad.khayal.utils.*


class HomeOptionSheet(
    private val binding: HomeOptionSheetBinding,
    private val fragment: Home
) {

    private val sheet = BottomSheetBehavior.from(binding.sheet)
    lateinit var sheetStatus: SheetStatus

    private val alert = AlertDialog.Builder(fragment.context)

    var notifyCounts: Int = 0
    var user: User? = null


    private fun setNotifyCount() {
        if (notifyCounts > 0) {
            binding.notify.badgeNumber.show()
            binding.notify.badgeNumber.text = notifyCounts.toString()
        } else {
            binding.notify.badgeNumber.hide()
        }
    }

    fun hideSheet() {
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
        sheetStatus.onClose()
    }

    fun showSheet() {
        binding.sheet.show()
        sheet.isDraggable = false
        sheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.user = user
        setViews()
        setNotifyCount()

        /** button profile **/
        binding.btnProfile.setOnClickListener {
            fragment.navigateToProfile()
            hideSheet()
        }

        /** button show users **/
        binding.btnUsers.setOnClickListener {
            fragment.navigateToUsers()
            hideSheet()
        }

        /** button show banner manager **/
        binding.btnBannerManager.setOnClickListener {
            fragment.navigateToBannerManager()
        }

        /** button  notifications **/
        binding.btnNotifications.setOnClickListener {
            fragment.navigateToNotification()
            hideSheet()
        }

        /** button settings **/
        binding.btnSettings.setOnClickListener {
            fragment.showMessage("settings")
        }

        /** button rate us **/
        binding.btnRateUs.setOnClickListener {
            fragment.showMessage("rate us")
        }

        /** button report **/
        binding.btnReport.setOnClickListener {
            showReportDialog()
        }

        /** button help & feedback **/
        binding.btnHelp.setOnClickListener {
            fragment.showMessage("help & feedback")
        }

        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

    }


    private fun setViews() {
        if (user?.userType == UserType.ADMIN.name) {
            binding.apply {
                btnSettings.hide()
                btnRateUs.hide()
                btnReport.hide()
                btnHelp.hide()
            }
        } else {
            binding.btnUsers.hide()
            binding.btnBannerManager.hide()
        }
    }


    private fun showReportDialog() {

        val title = fragment.context?.getString(R.string.report)
        var builder: AlertDialog? = null
        val edittext = EditText(fragment.context)
        edittext.background = null
        edittext.hint = fragment.context?.getString(R.string.hint_report)
        alert.setTitle(title)
        alert.setIcon(R.drawable.ic_report)

        alert.setCancelable(false)

        alert.setView(edittext)

        alert.setPositiveButton(fragment.context?.getString(R.string.send)) { dialog, whichButton ->
            val report = edittext.text.trim().toString()
            if (report.isNotEmpty()) {
                val notify = Notification(getNotifyId(), title!!, report, "", "", "", "")
                notify.pattern = NotifyPattern.Report.name
                notify.type = NotifyType.Direct.name
                notify.targetUser = Constants.ADMIN_ID
                sheetStatus.onReportSend(notify)
                builder?.dismiss()
            }
        }

        alert.setNegativeButton("cancel") { dialog, whichButton ->

            // what ever you want to do with No option.
            builder?.dismiss()
        }

        builder = alert.create()
        builder.show()

    }


    interface SheetStatus {
        fun onClose()
        fun onReportSend(notify: Notification)
    }


}