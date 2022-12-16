package com.shaimaziyad.khayal1.screens.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.shaimaziyad.khayal1.R
import com.shaimaziyad.khayal1.data.Notification
import com.shaimaziyad.khayal1.data.Novel
import com.shaimaziyad.khayal1.databinding.HomeBinding
import com.shaimaziyad.khayal1.screens.bannerManager.BannerManagerViewModel
import com.shaimaziyad.khayal1.screens.notifications.NotificationsViewModel
import com.shaimaziyad.khayal1.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal1.sheets.HomeOptionSheet
import com.shaimaziyad.khayal1.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


@SuppressLint("NotifyDataSetChanged")
class Home : Fragment() {

    private lateinit var binding: HomeBinding

    private val viewModel by sharedViewModel<HomeViewModel>()
    private val profileViewModel by sharedViewModel<ProfileViewModel>()
    private val notifyViewModel by sharedViewModel<NotificationsViewModel>()
    private val bannerViewModel by sharedViewModel<BannerManagerViewModel>()

    private lateinit var homeAdapter: HomeAdapter

    private lateinit var optionSheet: HomeOptionSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HomeBinding.inflate(layoutInflater)
        optionSheet = HomeOptionSheet(binding.optionsSheet, this) // home option sheet
        homeAdapter = HomeAdapter(this)


        MobileAds.initialize(requireContext()) {}

        setViews()
        setObserves()


        return binding.root
    }




    override fun onResume() {
        super.onResume()
        profileViewModel.loadUser()
        notifyViewModel.loadNotificationsByUserId()
        bannerViewModel.loadAds()
    }


    private fun setViews() {
        binding.apply {

            homeViewModel = viewModel
            profileModel = profileViewModel
            lifecycleOwner = this@Home

            setAd()

            setMixedAdapter()

            setToolBar()

            /** button add novel **/
            btnAddNovel.setOnClickListener {
                navigateToAddEditNovel(
                    isEdit = false,
                    novel = null
                ) // if isEdit false that means the user will add new novel else the user will edit old novel
            }


        }
    }

    private fun setAd() {

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }


    private fun setMixedAdapter() {
        binding.novelsRv.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = homeAdapter
        }
    }


    private fun setObserves() {
        viewModel.apply {


            /** mixed live data **/
            mixedData.observe(viewLifecycleOwner) { data ->
                if (!data.isNullOrEmpty()) {
                    homeAdapter.items = data
                    homeAdapter.notifyDataSetChanged()
                }
            }


            /** unRead live data **/
            notifyViewModel.unRead.observe(viewLifecycleOwner) { unReads ->
                if (!unReads.isNullOrEmpty()) {

                    binding.searchLayout.notify.badgeNumber.show()
                    binding.searchLayout.notify.badgeNumber.text = unReads.size.toString()
                } else {
                    binding.searchLayout.notify.badgeNumber.hide()
                }
            }


            /** banners **/
            bannerViewModel.banners.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmpty()) {

                }
            }
        }
    }


    private fun setHomeOptionSheetStatus() {
        optionSheet.sheetStatus = object : HomeOptionSheet.SheetStatus {
            override fun onClose() {
                binding.searchLayout.root.show()
                if (profileViewModel.user.value?.userType == UserType.ADMIN.name) {
                    binding.btnAddNovel.show()
                }
            }

            override fun onReportSend(notify: Notification) {
                notify.from = profileViewModel.user.value?.uid ?: ""
                notifyViewModel.pushNotify(notify)
                showMessage(resources.getString(R.string.success_report))
            }
        }
    }

    private fun setToolBar() {
        binding.searchLayout.apply {

            setHomeOptionSheetStatus()

            user = profileViewModel.user.value!!

            /** button profile **/
            btnProfile.setOnClickListener {
                val unRead = notifyViewModel.unRead.value!!.size
                val user = profileViewModel.user.value!!

                optionSheet.notifyCounts = unRead
                optionSheet.user = user
                binding.btnAddNovel.hide()
                binding.searchLayout.root.hide()
                optionSheet.showSheet()

            }


            /** button notification **/
            notify.root.setOnClickListener {
                navigateToNotification()
            }

            /** button search **/
            btnSearch.setOnClickListener {
                navigateToSearch(null)
            }


            /** let this here maybe we need it **/
//            /** button search **/
//            btnSearch.setOnEditorActionListener { textView, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    val query = textView.text.trim().toString()
//                    novelAdapter.submitList(viewModel.searchByNovelTitle(query))
//                    hideKeyboard()
//                    true
//                } else { false }
//            }


        }
    }


    fun navigateToSearch(category: List<Novel>?) {
        val data = bundleOf(Constants.CATEGORY_KEY to category)
        findNavController().navigate(R.id.action_home_to_search, data)
    }

    fun navigateToProfile() {
        findNavController().navigate(R.id.action_home_to_profile)
    }


    fun navigateToUsers() {
        findNavController().navigate(R.id.action_home_to_users)
    }


    fun navigateToNotification() {
        findNavController().navigate(R.id.action_home_to_notifications)
    }


    fun navigateToBannerManager() {
        findNavController().navigate(R.id.action_home_to_bannerManager)
    }

    fun navigateToAddEditNovel(isEdit: Boolean, novel: Novel?) {

        val userType = profileViewModel.user.value?.userType

        if (UserType.USER.name == userType) { // user
            val data = bundleOf(Constants.NOVEL_KEY to novel)
            findNavController().navigate(R.id.action_userHome_to_novelDetails, data)

        }
        if (userType == UserType.ADMIN.name) {
            val data = bundleOf(Constants.IS_EDIT_KEY to isEdit, Constants.NOVEL_KEY to novel)
            findNavController().navigate(R.id.action_home_to_addEditNovel, data)
        }

    }


}