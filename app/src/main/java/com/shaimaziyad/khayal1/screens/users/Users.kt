package com.shaimaziyad.khayal1.screens.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal1.R
import com.shaimaziyad.khayal1.data.Notification
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.databinding.UsersBinding
import com.shaimaziyad.khayal1.screens.notifications.NotificationsViewModel
import com.shaimaziyad.khayal1.sheets.FilterUserSheet
import com.shaimaziyad.khayal1.sheets.PushNotificationSheet
import com.shaimaziyad.khayal1.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class Users : Fragment() {

    private lateinit var binding: UsersBinding
    private val viewModel by sharedViewModel<UsersViewModel>()
    private val notifyViewModel by sharedViewModel<NotificationsViewModel>()

    private lateinit var pushNotifySheet: PushNotificationSheet
    private lateinit var filterUser: FilterUserSheet

    private val userAdapter by lazy { UserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = UsersBinding.inflate(layoutInflater)

        pushNotifySheet = PushNotificationSheet(binding.pushNotifySheet, this)
        filterUser = FilterUserSheet(requireContext(), binding.userFilterSheet, this)

        setViews()
        setObserves()

        return binding.root
    }

    private fun setObserves() {
        viewModel.apply {


            /** users live data **/
            users.observe(viewLifecycleOwner) { users ->
                if (!users.isNullOrEmpty()) {
                    userAdapter.submitList(users)
                    binding.refreshUsers.isRefreshing = false
                }
            }


        }
    }


    private fun setViews() {
        binding.apply {

            setAdapter()

            filterSheetStatus()
            notifySheetStatus()

            /** refresh layout users **/
            refreshUsers.setOnRefreshListener {
                viewModel.loadUsers()
            }

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            /** button options **/
            btnOptions.setOnClickListener {
                setUserOptions()
            }

            /** button search **/
            search.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.trim().toString()
                    userAdapter.submitList(viewModel.searchByUserName(query))
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }


        }
    }

    private fun setUserOptions() {
        val popupMenu = PopupMenu(requireContext(), binding.btnOptions)
        popupMenu.menuInflater.inflate(R.menu.users_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_filter -> {
                    filterUser.users = viewModel.users.value ?: emptyList()
                    filterUser.showSheet()
                }
                R.id.item_pushNotification -> {
                    pushNotifySheet.showSheet()
                }
            }
            true
        }
        popupMenu.show()
    }

    private fun filterSheetStatus() {

        filterUser.filterStatus = object : FilterUserSheet.FilterStatus {
            override fun onFilter(filtered: List<User>) {
                userAdapter.submitList(filtered)
            }

            override fun clearFilter() {
                userAdapter.submitList(viewModel.users.value ?: emptyList())
            }

        }
    }


    private fun notifySheetStatus() {

        pushNotifySheet.notifyStatus = object : PushNotificationSheet.NotifyStatus {
            override fun onSend(notify: Notification) {
                notify.pattern = NotifyPattern.Alert.name
                notify.type = NotifyType.System.name
                notifyViewModel.pushNotify(notify)
                showMessage("notify send")
                pushNotifySheet.hideSheet()
            }

        }
    }


    private fun setAdapter() {

        /** onNovelClick listener **/
        userAdapter.clickListener = object : UserAdapter.UserClickListener {

            override fun onClick(user: User, index: Int) {
                val data = bundleOf(Constants.IS_USER_NOTIFY to true, Constants.USER_KEY to user)
                findNavController().navigate(R.id.action_users_to_profile, data)

            }

        }
        binding.usersRv.adapter = userAdapter
    }


}