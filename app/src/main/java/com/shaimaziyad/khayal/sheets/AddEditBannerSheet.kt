package com.shaimaziyad.khayal.sheets


import android.net.Uri
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Banner
import com.shaimaziyad.khayal.databinding.AddEditBannerSheetBinding
import com.shaimaziyad.khayal.utils.*
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult

class AddEditBannerSheet(
    private val binding: AddEditBannerSheetBinding,
    private val fragment: Fragment
) {

    lateinit var bannerStatus: BannerStatus
    private val context = fragment.requireContext()

    private val sheet = BottomSheetBehavior.from(binding.sheet)
    private var imgBanner: Uri? = null
    private val imagePicker = ImagePicker(fragment)
    private var type = AdType.Rotating.name // by default


    private fun resetSheet() {
        binding.apply {
            btnBannerState.isChecked = false
            title.setText("")
            description.setText("")
            type = AdType.Rotating.name // by default
            typeRotating.isChecked = true
            typeBanner.isChecked = false
            imgBanner = null
            imgCover.setImageResource(0) // clear image content
        }
    }

    fun hideSheet() {
        bannerStatus.onClose()
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
    }

    fun showSheet(banner: Banner?, isEdit: Boolean) {
        bannerStatus.onShow()

        binding.sheet.show()
        sheet.isDraggable = false
        sheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.isEdit = isEdit

        if (banner != null) { // isEdit
            binding.data = banner
            binding.btnBannerState.isChecked = banner.isActive
            imgBanner = banner.cover.toUri()
            binding.btnRemove.show()

            setLabel(context.getString(R.string.edit_banner))
            setBannerType(banner)
        } else { // add new
            resetSheet()
            setLabel(context.getString(R.string.add_Banner))
        }


        /** radio buttons **/
        binding.radioGroupAdType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.typeBanner -> {
                    type = AdType.Banner.name
                }
                R.id.typeRotating -> {
                    type = AdType.Rotating.name
                }

            }
        }


        /** button add edit banner **/
        binding.btnAddUpdate.setOnClickListener {
            val title = binding.title.text?.trim().toString()
            val description = binding.description.text?.trim().toString()
            val isActive = binding.btnBannerState.isChecked // status of banner

            if (title.isEmpty()) {
                binding.title.error = context.getString(R.string.error_require_title)
                binding.title.requestFocus()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                binding.description.error = context.getString(R.string.error_require_description)
                binding.description.requestFocus()
                return@setOnClickListener
            }
            if (type.isEmpty()) {
                fragment.showMessage(context.getString(R.string.error_require_type))
            }
            if (imgBanner == null) {
                fragment.showMessage(context.getString(R.string.error_require_image))
            }

            if (type.isNotEmpty() && imgBanner != null) {
                banner?.cover = imgBanner.toString()
                banner?.isActive = isActive
                banner?.type = type

                if (isEdit) { // update old banner
                    showProgress()
                    bannerStatus.update(banner!!)
                } else { // add new banner
                    showProgress()
                    val newBanner = Banner(
                        getBannerId(),
                        title,
                        description,
                        imgBanner.toString(),
                        type,
                        isActive
                    )
                    bannerStatus.add(newBanner)
                }

            }

        }

        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

        /** button select image profile **/
        binding.btnAdCover.setOnClickListener {
            imagePicker.pickFromStorage { imageResult ->
                imageCallBack(imageResult)
            }
        }

        /** button delete banner **/
        binding.btnRemove.setOnClickListener {
            bannerStatus.delete(banner!!)
        }

    }

    private fun setLabel(t: String) {
        binding.tvLabel.text = t
    }

    private fun setBannerType(banner: Banner) {
        when (banner.type) {
            AdType.Banner.name -> {
                binding.typeBanner.isChecked = true
                binding.typeRotating.isChecked = false
            }
            AdType.Rotating.name -> {
                binding.typeBanner.isChecked = false
                binding.typeRotating.isChecked = true
            }
        }
        type = banner.type
    }


    private fun showProgress() {
        binding.loader.root.show()
    }

    fun hideProgress() {
        binding.loader.root.hide()
    }


    private fun imageCallBack(imageResult: ImageResult<Uri>) {
        when (imageResult) {
            is ImageResult.Success -> {
                val uri = imageResult.value
                imgBanner = uri
                binding.imgCover.setImageURI(uri)
            }
            is ImageResult.Failure -> {
                val error = imageResult.errorString
                fragment.showMessage(error)
            }
        }
    }


    interface BannerStatus {
        fun onShow()
        fun onClose()
        fun add(banner: Banner)
        fun update(banner: Banner)
        fun delete(banner: Banner)

    }

}