package com.shaimaziyad.khayal.screens.addEditBanner

import android.media.Image
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Banner
import com.shaimaziyad.khayal.databinding.AddEditBannerBinding
import com.shaimaziyad.khayal.screens.bannerManager.BannerManagerViewModel
import com.shaimaziyad.khayal.utils.*
import com.shaimaziyad.khayal.utils.getBannerId
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import org.koin.android.viewmodel.ext.android.sharedViewModel


class AddEditBanner : Fragment() {
    private lateinit var binding: AddEditBannerBinding
    private val viewModel by sharedViewModel<BannerManagerViewModel>()
    private var banner: Banner? = null

    private lateinit var imagePicker: ImagePicker

    private var isActive = false
    private var mTitle = ""
    private var mDescription = ""
    private var mType = AdType.Rotating.name // by default
    private var mCover: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AddEditBannerBinding.inflate(inflater, container, false)

        imagePicker = ImagePicker(this)
        banner = try {
            arguments?.get(Constants.BANNER_KEY) as Banner
        } catch (ex: Exception) {
            null
        }


        setViews()
        setObserves()



        return binding.root
    }


    private fun setViews() {
        binding.apply {


            setDataToViews()

            /** button add update banner **/
            btnAddUpdate.setOnClickListener {
                mTitle = binding.title.text?.trim().toString()
                mDescription = binding.description.text?.trim().toString()
                isActive = binding.btnBannerState.isChecked // status of banner
//                if (mTitle.isEmpty()) {
//                    binding.title.error = getString(R.string.error_require_title)
//                    binding.title.requestFocus()
//                    return@setOnClickListener
//                }
//                if (mDescription.isEmpty()) {
//                    binding.description.error = getString(R.string.error_require_description)
//                    binding.description.requestFocus()
//                    return@setOnClickListener
//                }
                if (mType.isEmpty()) {
                    showMessage(getString(R.string.error_require_type))

                }
                if (mCover.isEmpty()) {
                    showMessage(getString(R.string.error_require_image))
                } else {
                    subMit()
                }


            }


            /** button remove banner **/
            btnRemove.setOnClickListener {
                viewModel.deleteBanner(banner!!)
            }


            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }


            /** button add cover **/
            btnAdCover.setOnClickListener {
                imagePicker.pickFromStorage { imageResult ->
                    imageCallBack(imageResult)
                }
            }


            /** radio buttons **/
            binding.radioGroupAdType.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.typeBanner -> mType = AdType.Banner.name
                    R.id.typeRotating -> mType = AdType.Rotating.name
                }
            }


        }
    }


    private fun setObserves() {
        viewModel.apply {

            /** upload status **/
            viewModel.uploadStatus.observe(viewLifecycleOwner) {
                when (it) {
                    DataStatus.LOADING -> {
                        binding.loader.root.show()
                    }
                    DataStatus.SUCCESS -> {
                        binding.loader.root.hide()
                        showMessage("uploaded")
                        resetStatus()
                        findNavController().navigateUp()
                    }
                    DataStatus.ERROR -> {
                        binding.loader.root.hide()
                        resetStatus()
                    }
                    else -> {}
                }
            }

            /** update status **/
            viewModel.updateStatus.observe(viewLifecycleOwner) {
                when (it) {
                    DataStatus.LOADING -> {
                        binding.loader.root.show()
                    }
                    DataStatus.SUCCESS -> {
                        showMessage("updated")
                        binding.loader.root.hide()
                        resetStatus()
                        findNavController().navigateUp()
                    }
                    DataStatus.ERROR -> {
                        binding.loader.root.hide()
                        resetStatus()
                    }
                    else -> {}
                }
            }

            /** delete status **/
            viewModel.deleteStatus.observe(viewLifecycleOwner) {
                when (it) {
                    DataStatus.LOADING -> {
                        binding.loader.root.show()
                    }
                    DataStatus.SUCCESS -> {
                        showMessage("deleted")
                        binding.loader.root.hide()
                        resetStatus()
                        findNavController().navigateUp()
                    }
                    DataStatus.ERROR -> {
                        binding.loader.root.hide()
                        resetStatus()
                    }
                    else -> {}
                }
            }


        }
    }

    private fun subMit() {
        if (banner != null) { // update old banner
            banner!!.title = mTitle
            banner!!.description = mDescription
            banner!!.type = mType
            banner!!.isActive = isActive
            banner!!.cover = mCover
            viewModel.updateBanner(banner!!)
        } else { // add new banner
            val newBanner = Banner(getBannerId(), mTitle, mDescription, mCover, mType, isActive)
            viewModel.uploadBanner(newBanner)
        }
    }

    private fun imageCallBack(imageResult: ImageResult<Uri>) {
        when (imageResult) {
            is ImageResult.Success -> {
                val uri = imageResult.value
                mCover = uri.toString()
                binding.imgCover.setImageURI(uri)
            }
            is ImageResult.Failure -> {
                val error = imageResult.errorString
            }
        }
    }

    private fun setDataToViews() {
        binding.apply {
            if (banner != null) {

                binding.data = banner
                binding.btnBannerState.isChecked = banner!!.isActive
                mCover = banner?.cover!!
                mType = banner!!.type
                binding.btnRemove.show()

                btnAddUpdate.text = getString(R.string.update)
                setLabel(getString(R.string.edit_banner))

                if (banner!!.type == AdType.Rotating.name) {
                    binding.typeRotating.isChecked = true
                } else {
                    binding.typeBanner.isChecked = true
                }

            } else {
                binding.typeRotating.isChecked = true
                btnAddUpdate.text = getString(R.string.add_Banner)
                setLabel(getString(R.string.add_Banner))
            }

        }
    }


    private fun setLabel(t: String) {
        binding.tvLabel.text = t
    }


}