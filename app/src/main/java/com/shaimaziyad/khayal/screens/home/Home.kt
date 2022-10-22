package com.shaimaziyad.khayal.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.HomeBinding
import com.shaimaziyad.khayal.screens.notifications.NotificationsViewModel
import com.shaimaziyad.khayal.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal.sheets.FilterNovelSheet
import com.shaimaziyad.khayal.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class Home : Fragment() {

    companion object {
        private const val ITEM_USERS = 0
    }

    private lateinit var binding: HomeBinding

    private val viewModel by sharedViewModel<HomeViewModel>()

    private val profileViewModel by sharedViewModel<ProfileViewModel>()

    private val notifyViewModel by sharedViewModel<NotificationsViewModel>()

    private val novelAdapter by lazy { NovelAdapter() }
    private lateinit var filterNovel: FilterNovelSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HomeBinding.inflate(layoutInflater)
        filterNovel = FilterNovelSheet(requireContext(), binding.novelSheet, this)


        // todo: add share pref for user between screens

        setViews()
        setObserves()



        return binding.root
    }


    override fun onResume() {
        super.onResume()

        val isFirstLoad = viewModel.novels.value.isNullOrEmpty()
        if (!isFirstLoad) { // show progress only for first time load

        }

        viewModel.loadNovels()
        profileViewModel.loadUser()
        notifyViewModel.loadNotificationsByUserId()


    }


    private fun setViews() {
        binding.apply {

            homeViewModel = viewModel
            profileModel = profileViewModel
            lifecycleOwner = this@Home

            setAdapter()
            setToolBar()

            filterNovelStatus()

            /** button add novel **/
            btnAddNovel.setOnClickListener {
                navigateToAddEditNovel(
                    isEdit = false,
                    novel = null
                ) // if isEdit false that means the user will add new novel else the user will edit old novel
            }

            /** swipe to refresh novels **/
            refreshNovels.setOnRefreshListener {
                viewModel.loadNovels()
                profileViewModel.loadUser()
                notifyViewModel.loadNotificationsByUserId()
            }
        }
    }


    private fun setObserves() {
        viewModel.apply {

            /** novels live data **/
            novels.observe(viewLifecycleOwner) { novels ->
                if (!novels.isNullOrEmpty()) { // novel is exist.
                    novelAdapter.submitList(novels)
                }
            }


            /** unRead live data **/
            notifyViewModel.unRead.observe(viewLifecycleOwner) { unReads ->
                if (!unReads.isNullOrEmpty()) {
                    binding.notify.badgeNumber.show()
                    binding.notify.badgeNumber.text = unReads.size.toString()
                } else {
                    binding.notify.badgeNumber.hide()
                }
            }


        }
    }

    private fun filterNovelStatus() {
        filterNovel.filterStatus = object : FilterNovelSheet.FilterStatus {

            override fun onFilter(filtered: List<NovelData>) {
                novelAdapter.submitList(filtered)
            }

            override fun clearFilter() {
                novelAdapter.submitList(viewModel.novels.value ?: emptyList())
            }

            override fun onSheetOpen() {
                binding.btnAddNovel.hide()
            }

            override fun onSheetClose() {
                if (profileViewModel.user.value?.userType == UserType.ADMIN.name) {
                    binding.btnAddNovel.show()
                }

            }

        }
    }

    private fun setToolBar() {
        binding.apply {


            /** button profile **/
            btnProfile.setOnClickListener {
                navigateToProfile()
            }

            /** button options **/
            btnOptions.setOnClickListener {
                setHomeOption()
            }


            /** button notification **/
            notify.btnNotify.setOnClickListener {
                navigateToNotification()
            }


            /** button search **/
            search.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.trim().toString()
                    novelAdapter.submitList(viewModel.searchByNovelTitle(query))
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }


        }
    }


    private fun navigateToProfile() {
        findNavController().navigate(R.id.action_home_to_profile)
    }


    private fun navigateToUsers() {
        findNavController().navigate(R.id.action_home_to_users)
    }


    private fun navigateToNotification() {
        findNavController().navigate(R.id.action_home_to_notifications)
    }

    private fun setHomeOption() {
        val popupMenu = PopupMenu(requireContext(), binding.btnOptions)
        popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

        if (!viewModel.isCustomer.value!!) { // if admin
            popupMenu.menu.add(Menu.NONE, ITEM_USERS, Menu.NONE, R.string.users) // add item to menu

        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_filter -> {
                    filterNovel.novels = viewModel.novels.value ?: emptyList()
                    filterNovel.showSheet()
                }
                ITEM_USERS -> {
                    navigateToUsers()
                }
            }
            true
        }
        popupMenu.show()
    }


    private fun navigateToAddEditNovel(isEdit: Boolean, novel: NovelData?) {
        val data = bundleOf(Constants.IS_EDIT_KEY to isEdit, Constants.NOVEL_KEY to novel)
        findNavController().navigate(R.id.action_home_to_addEditNovel, data)
    }


    private fun setAdapter() {

        /** onNovelClick listener **/
        novelAdapter.clickListener = object : NovelAdapter.NovelClickListener {

            override fun onClick(novel: NovelData, index: Int) {
                val userType = profileViewModel.user.value?.userType

                if (UserType.USER.name == userType) { // user
                    val data = bundleOf(Constants.NOVEL_KEY to novel)
                    findNavController().navigate(R.id.action_userHome_to_novelDetails, data)

                }
                if (userType == UserType.ADMIN.name) {
                    navigateToAddEditNovel(true, novel)
                }

            }
        }

        binding.novelsRv.adapter = novelAdapter
    }


}