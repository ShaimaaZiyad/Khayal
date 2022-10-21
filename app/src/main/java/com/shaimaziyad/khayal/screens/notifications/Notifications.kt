package com.shaimaziyad.khayal.screens.notifications

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Notification
import com.shaimaziyad.khayal.databinding.NotificationsBinding
import com.shaimaziyad.khayal.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

@SuppressLint("ResourceAsColor")
class Notifications: Fragment() {


    private val viewModel by sharedViewModel<NotificationsViewModel>()
    private lateinit var binding: NotificationsBinding
    private val notifyAdapter by lazy { NotifyAdapter() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = NotificationsBinding.inflate(inflater,container,false)


        setViews()
        setObserves()

        return binding.root
    }

    override fun onResume() {
        super.onResume()


        unReadSelected()

    }


    private fun setViews() {
        binding.apply {

            setAdapter()






            /** back button **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            /** button option **/
            btnOptions.setOnClickListener {
                showMessage("options")
            }

            /** button unRead **/
            btnUnRead.setOnClickListener {
                unReadSelected()
            }

            /** button Read **/
            btnRead.setOnClickListener {
                readSelected()

            }

            /** button System **/
            btnSystem.setOnClickListener {
                systemSelected()
            }

        }
    }

    private fun setAdapter() {
        /** onNovelClick listener **/
        notifyAdapter.clickListener = object: NotifyAdapter.NotifyClickListener {

            override fun onClick(notify: Notification, index: Int) {
                showMessage("onClick")
            }

            override fun onLongClick(notify: Notification, index: Int, v: View) {
                if (notify.type == NotifyType.Direct.name){
                    setNotifyItemMenu(notify,v)
                }

            }


        }

        binding.rvNotification.adapter = notifyAdapter
    }


    private fun setNotifyItemMenu(notify: Notification, v: View) {
        val popupMenu = PopupMenu(requireContext(),v)
        popupMenu.menuInflater.inflate(R.menu.item_notify_menu,popupMenu.menu)

        when(notify.type){
            NotifyType.Direct.name -> {
                if (notify.isRead == false) { // unRead
                    popupMenu.menu.removeItem(R.id.item_unRead)
                }
                if (notify.isRead == true) { // read
                    popupMenu.menu.removeItem(R.id.item_read)
                }
            }
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_unRead -> {
                    notify.isRead = false
                    viewModel.updateNotify(notify)
                }
                R.id.item_read -> {
                    notify.isRead = true
                    viewModel.updateNotify(notify)
                }
                R.id.item_remove -> {

                    viewModel.removeNotify(notify)
                }
            }
            true
        }
        popupMenu.show()
    }


    private fun setObserves() {
        viewModel.apply {

        }

    }




    private fun unReadSelected() {
        resetStatus()


        binding.apply {
            btnUnRead.setBackgroundColor(Color.parseColor("#0B4953"))
            tvUnRead.setTextColor(Color.parseColor("#FFFFFF"))
            tvUnRead.typeface = Typeface.DEFAULT_BOLD
        }
        notifyAdapter.submitList(viewModel.filter(NotifyType.Direct.name,isRead = false))
    }

    private fun readSelected() {
        resetStatus()


        binding.apply {
            btnRead.setBackgroundColor(Color.parseColor("#0B4953"))
            tvRead.setTextColor(Color.parseColor("#FFFFFF"))
            tvRead.typeface = Typeface.DEFAULT_BOLD
        }
        notifyAdapter.submitList(viewModel.filter(NotifyType.Direct.name,isRead = true))

    }

    private fun systemSelected() {
        resetStatus()

        binding.apply {
            btnSystem.setBackgroundColor(Color.parseColor("#0B4953"))
            tvSystem.setTextColor(Color.parseColor("#FFFFFF"))
            tvSystem.typeface = Typeface.DEFAULT_BOLD
        }
        notifyAdapter.submitList(viewModel.filter(NotifyType.System.name,isRead = null))
    }

    private fun resetStatus() {
        binding.apply {
            btnUnRead.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tvUnRead.setTextColor(Color.parseColor("#777777"))
            tvUnRead.typeface = Typeface.DEFAULT

            btnRead.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tvRead.setTextColor(Color.parseColor("#777777"))
            tvRead.typeface = Typeface.DEFAULT

            btnSystem.setBackgroundColor(Color.parseColor("#FFFFFF"))
            tvSystem.setTextColor(Color.parseColor("#777777"))
            tvSystem.typeface = Typeface.DEFAULT
        }

    }





}