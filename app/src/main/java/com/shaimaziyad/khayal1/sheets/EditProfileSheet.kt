package com.shaimaziyad.khayal1.sheets


import android.net.Uri
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.shaimaziyad.khayal1.data.User
import com.shaimaziyad.khayal1.databinding.EditProfileSheetBinding
import com.shaimaziyad.khayal1.utils.hide
import com.shaimaziyad.khayal1.utils.hideKeyboard
import com.shaimaziyad.khayal1.utils.show
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult

class EditProfileSheet(
    private val binding: EditProfileSheetBinding,
    private val fragment: Fragment
) {

    lateinit var editProfileStatus: EditProfileStatus

    private val sheet = BottomSheetBehavior.from(binding.sheet)
    private var imgProfile: Uri? = null
    private val imagePicker = ImagePicker(fragment)


    fun hideSheet() {
        sheet.state = BottomSheetBehavior.STATE_COLLAPSED
        fragment.hideKeyboard()
        binding.sheet.hide()
    }

    fun showSheet(oldUser: User) {
        binding.sheet.show()
        sheet.isDraggable = false
        sheet.state = BottomSheetBehavior.STATE_EXPANDED
        binding.user = oldUser

        /** button update **/
        binding.btnUpdate.setOnClickListener {
            val name = binding.name.text?.trim().toString()
            oldUser.name = name
            if (imgProfile != null) { // update profile
                oldUser.profileImage = imgProfile.toString() // set the image uri as string
            }
            editProfileStatus.update(oldUser)
        }


        /** button close **/
        binding.btnClose.setOnClickListener {
            hideSheet()
        }

        /** button select image profile **/
        binding.btnImageProfile.setOnClickListener {
            imagePicker.pickFromStorage { imageResult ->
                imageCallBack(imageResult)
            }
        }
    }


    fun showProgress() {
        binding.loader.root.show()
    }

    fun hideProgress() {
        binding.loader.root.hide()
    }


    private fun imageCallBack(imageResult: ImageResult<Uri>) {
        when (imageResult) {
            is ImageResult.Success -> {
                val uri = imageResult.value
                imgProfile = uri
                binding.btnImageProfile.setImageURI(uri)
            }
            is ImageResult.Failure -> {
                val error = imageResult.errorString
            }
        }
    }


    interface EditProfileStatus {
        fun update(user: User)
    }

}