package com.shaimaziyad.khayal1.screens.bannerManager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal1.R
import com.shaimaziyad.khayal1.data.Banner
import com.shaimaziyad.khayal1.databinding.BannerManagerBinding
import com.shaimaziyad.khayal1.utils.Constants
import com.shaimaziyad.khayal1.utils.DataStatus
import org.koin.android.viewmodel.ext.android.sharedViewModel

class BannerManager : Fragment() {


    private val viewModel by sharedViewModel<BannerManagerViewModel>()

    private lateinit var binding: BannerManagerBinding

    //    private lateinit var addEditBannerSheet: AddEditBannerSheet
    private val bannerAdapter by lazy { BannerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BannerManagerBinding.inflate(inflater, container, false)
//        addEditBannerSheet = AddEditBannerSheet(binding.bannerSheet,this)

        setViews()
        setObserves()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

//        viewModel.getBanners()
    }

    private fun setObserves() {


        /** banners **/
        viewModel.banners.observe(viewLifecycleOwner) { data ->
            if (!data.isNullOrEmpty()) {
                bannerAdapter.submitList(data)
//                showMessage("banners: ${data.size}")
            }
        }


        /** banner status **/
        viewModel.loadStatus.observe(viewLifecycleOwner) {
            when (it) {
                DataStatus.LOADING -> {}
                DataStatus.SUCCESS -> {

                }
                DataStatus.ERROR -> {

                }
                else -> {}

            }
        }

    }


    private fun setViews() {
        binding.apply {


            bannerManagerViewModel = viewModel
            lifecycleOwner = this@BannerManager


            setAdapter()
//            setBannerStatus()

            /** button add new banner **/
            btnAddBanner.setOnClickListener {
                navigateToAddEditBanner(null, isEdit = false)
//                addEditBannerSheet.showSheet(null,isEdit = false)
            }


            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }


        }
    }


    private fun setAdapter() {
        /** on banner clicked **/
        bannerAdapter.clickListener = object : BannerAdapter.BannerClickListener {
            override fun onEditBanner(banner: Banner) {
                navigateToAddEditBanner(banner, isEdit = true)
//                addEditBannerSheet.showSheet(banner, isEdit = true)
            }
        }
        binding.rvBanners.adapter = bannerAdapter
    }


    private fun navigateToAddEditBanner(banner: Banner?, isEdit: Boolean) {
        val data = bundleOf(Constants.BANNER_KEY to banner, Constants.IS_EDIT_KEY to isEdit)
        findNavController().navigate(R.id.action_bannerManager_to_addEditBanner, data)
    }


//    private fun setBannerStatus(){
//        addEditBannerSheet.bannerStatus = object: AddEditBannerSheet.BannerStatus{
//            override fun onShow() {
//                binding.btnAddBanner.hide()
//            }
//
//            override fun onClose() {
//                binding.btnAddBanner.show()
//            }
//
//            override fun add(banner: Banner) {
//                viewModel.uploadBanner(banner)
//                showMessage("add")
//            }
//
//            override fun update(banner: Banner) {
//                viewModel.updateBanner(banner)
//                showMessage("update")
//            }
//
//            override fun delete(banner: Banner) {
//                viewModel.deleteBanner(banner)
//                showMessage("delete")
//            }
//
//        }
//    }


}