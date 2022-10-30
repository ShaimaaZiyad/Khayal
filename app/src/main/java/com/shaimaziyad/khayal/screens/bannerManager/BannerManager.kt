package com.shaimaziyad.khayal.screens.bannerManager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shaimaziyad.khayal.databinding.BannerManagerBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class BannerManager : Fragment() {


    private val viewModel by sharedViewModel<BannerManagerViewModel>()
    private lateinit var binding: BannerManagerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = BannerManagerBinding.inflate(inflater,container,false)

        setViews()
        setObserves()

        return binding.root
    }


    private fun setObserves() {

    }

    private fun setViews() {

    }


}