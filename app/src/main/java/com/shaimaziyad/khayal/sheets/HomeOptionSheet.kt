package com.shaimaziyad.khayal.sheets


import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.databinding.HomeOptionSheetBinding
import com.shaimaziyad.khayal.screens.home.Home
import com.shaimaziyad.khayal.utils.*

class HomeOptionSheet(private val binding: HomeOptionSheetBinding,
                      private val fragment: Home) {

    private val sheet = BottomSheetBehavior.from(binding.sheet)

    var notifyCounts: Int = 0
    var user: User? = null


    private fun setNotifyCount(){
        if (notifyCounts > 0){
            binding.notify.badgeNumber.show()
            binding.notify.badgeNumber.text = notifyCounts.toString()
        }else{
            binding.notify.badgeNumber.hide()
        }
    }

    fun hideSheet() {
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
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
//            fragment.findNavController().navigate(R.id.action_home_to_profile)
            hideSheet()
        }

        /** button  notifications **/
        binding.btnUsers.setOnClickListener {
//            fragment.findNavController().navigate(R.id.action_home_to_users)
            fragment.navigateToUsers()
            hideSheet()
        }


        /** button  notifications **/
        binding.btnNotifications.setOnClickListener {
//            fragment.findNavController().navigate(R.id.action_home_to_notifications)
            fragment.navigateToNotification()
            hideSheet()
        }

        /** button settings **/
        binding.btnSettings.setOnClickListener{
            fragment.showMessage("settings")
        }

        /** button rate us **/
        binding.btnRateUs.setOnClickListener {
            fragment.showMessage("rate us")
        }

        /** button report **/
        binding.btnReport.setOnClickListener {
            fragment.showMessage("report")
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
        if (user?.userType == UserType.ADMIN.name){
            binding.apply {
                btnNotifications.hide()
                btnSettings.hide()
                btnRateUs.hide()
                btnReport.hide()
                btnHelp.hide()
            }
        }else{
            binding.btnUsers.hide()
        }
    }



}