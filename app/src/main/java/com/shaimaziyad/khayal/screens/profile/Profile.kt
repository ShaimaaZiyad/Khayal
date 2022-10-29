package com.shaimaziyad.khayal.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.data.User
import com.shaimaziyad.khayal.databinding.ProfileBinding
import com.shaimaziyad.khayal.notification.sendNotification
import com.shaimaziyad.khayal.screens.home.HomeViewModel
import com.shaimaziyad.khayal.screens.notifications.NotificationsViewModel
import com.shaimaziyad.khayal.screens.search.SearchAdapter
import com.shaimaziyad.khayal.sheets.EditProfileSheet
import com.shaimaziyad.khayal.sheets.PushNotificationSheet
import com.shaimaziyad.khayal.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import kotlin.properties.Delegates


class Profile : Fragment() {

    private lateinit var binding: ProfileBinding

    private val viewModel by sharedViewModel<ProfileViewModel>()
    private val homeViewModel by sharedViewModel<HomeViewModel>()
    private val notifyViewModel by sharedViewModel<NotificationsViewModel>()

    private lateinit var profileSheet: EditProfileSheet
    private lateinit var pushNotifySheet: PushNotificationSheet
    private val searchAdapter by lazy { SearchAdapter() }

    private var isUserNotify by Delegates.notNull<Boolean>()
    private var user: User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = ProfileBinding.inflate(layoutInflater)

        setData()

        profileSheet = EditProfileSheet(binding.profileSheet,this)
        pushNotifySheet = PushNotificationSheet(binding.pushNotifySheet, this)

        setViews()
        setObserves()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val likedNovels = ArrayList<Novel>()
        val liked = viewModel.user.value?.likes
        liked?.forEach { novelId ->
            val novel = homeViewModel.novels.value?.find { it.novelId == novelId }
            if (novel != null){
                likedNovels.add(novel)
            }
        }

//        showMessage("liked: ${likedNovels.size}")
        searchAdapter.submitList(likedNovels)
//        searchAdapter.notifyDataSetChanged()

    }

    private fun refreshLikedNovels(){

    }

    private fun setObserves() {
        viewModel.apply {

            /** isLoggedOut live data **/
            isLoggedOut.observe(viewLifecycleOwner) {
                if (it == true){
                    findNavController().navigate(R.id.action_profile_to_Login)
                    viewModel.resetStatus()

                }
            }


            /** update user Status **/
            viewModel.updateUserState.observe(viewLifecycleOwner) { status->
                if (status != null) {
                    when(status){
                        DataStatus.LOADING-> {
                            profileSheet.showProgress()
                        }
                        DataStatus.SUCCESS-> {
                            profileSheet.hideSheet()
                            profileSheet.hideProgress()
                            viewModel.resetStatus()
                        }
                        DataStatus.ERROR-> {
                            profileSheet.hideProgress()
                            viewModel.resetStatus()

                        }
                    }
                }
            }

            /** push Notify Status **/
            notifyViewModel.notifyStatus.observe(viewLifecycleOwner){ status ->
                when(status){
                    DataStatus.LOADING-> {

                    }
                    DataStatus.SUCCESS-> {
                        showMessage(resources.getString(R.string.notify_send))
                        viewModel.resetStatus()
                        pushNotifySheet.hideSheet()
                    }
                    DataStatus.ERROR-> {
                        viewModel.resetStatus()
                        pushNotifySheet.hideSheet()
                    }
                    else -> {}
                }

            }



        }
    }


    private fun setFavoritesAdapter() {
        searchAdapter.clickListener = object: SearchAdapter.ClickListener{
            override fun onClick(novel: Novel, index: Int) {
                val data = bundleOf(Constants.NOVEL_KEY to novel)
                findNavController().navigate(R.id.action_profile_to_novelDetails,data)
//                showMessage(novel.title)
            }
        }
        binding.rvNovels.adapter = searchAdapter
    }


    // todo: add option for remove image profile

    private fun setViews() {
        binding.apply {


//            profileViewModel = viewModel
            userModel = if (user != null){ user }
            else{ viewModel.user.value }

            profileViewModel = viewModel
            lifecycleOwner = this@Profile

            setFavoritesAdapter()

            editProfileSheetStatus()
            notifySheetStatus()

            /** button option **/
            btnOptions.setOnClickListener {
                setOption()
            }

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }


        }
    }

    private fun editProfileSheetStatus() {
        profileSheet.editProfileStatus = object : EditProfileSheet.EditProfileStatus{

            override fun update(user: User) {
                viewModel.update(user)
                binding.userModel = user
                hideKeyboard()
            }

        }
    }

    private fun notifySheetStatus() {
        pushNotifySheet.notifyStatus = object : PushNotificationSheet.NotifyStatus {

            override fun onSend(notify: Notification) {

                if (!user?.token.isNullOrEmpty()) {
                    sendNotification(notify,user?.token!!)
                    notify.type = NotifyType.Direct.name
                    notify.pattern = NotifyPattern.Alert.name
                    notifyViewModel.pushNotify(notify)
                }else {
                    showMessage(resources.getString(R.string.error_no_token))
                }

            }

        }
    }


    private fun setOption() {
        val popupMenu = PopupMenu(requireContext(),binding.btnOptions)
        popupMenu.menuInflater.inflate(R.menu.profile_menu,popupMenu.menu)
        if (isUserNotify != true) { // remove item only if the user view his profile
            popupMenu.menu.removeItem(R.id.item_pushNotification)
        }
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_editProfile -> {
                    if (user != null){
                        profileSheet.showSheet(user!!)
                    }else{
                        profileSheet.showSheet(viewModel.user.value!!)
                    }

                }
                R.id.item_pushNotification -> {
                    pushNotifySheet.userId = user?.uid ?: ""
                    pushNotifySheet.showSheet()
                }
                R.id.item_signOut -> { viewModel.signOut() }
            }
            true
        }
        popupMenu.show()
    }

    private fun setData(){
        isUserNotify = try {arguments?.get(Constants.IS_USER_NOTIFY) as Boolean } catch (ex: Exception){false}
        user = try { arguments?.get(Constants.USER_KEY) as User }catch (ex: Exception) { null }
    }

}