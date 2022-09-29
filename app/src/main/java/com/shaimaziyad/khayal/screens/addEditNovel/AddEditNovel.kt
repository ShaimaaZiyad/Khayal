package com.shaimaziyad.khayal.screens.addEditNovel

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.AddEditNovelBinding
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import com.karumi.dexter.listener.PermissionRequest
import com.shaimaziyad.khayal.utils.*
import com.shaimaziyad.khayal.utils.getNovelId


class AddEditNovel : Fragment() {

    companion object{
        const val TAG = "AddEditNovel"
    }

    private lateinit var binding: AddEditNovelBinding
    private lateinit var viewModel: AddEditNovelViewModel

    private var isEdit = false

    private var novel: NovelData? = null
    private var mtitle = ""
    private var mDescription = ""
    private var mWriter = ""
    private var mtype = ""
    private var mCategory = ""
    private var mCover = ""
    private var mPdfs = ArrayList<Uri>()

    private val pdfAdapter by lazy { AdapterPdf() }
    private var pdfFile: Uri? = null
    private lateinit var bottomSheetPdf: BottomSheetDialog
    private lateinit var imagePicker: ImagePicker



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddEditNovelBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AddEditNovelViewModel::class.java]
        imagePicker = ImagePicker(this)
        bottomSheetPdf = BottomSheetDialog(requireContext())

        setData()
        setViews()
        checkPermission()
        setObserves()



        return binding.root
    }

    private fun setObserves() {
        viewModel.apply {

            /** navigate to home live data **/
            navigateToHome.observe(viewLifecycleOwner) {
                if (it == true){
                    findNavController().navigate(R.id.action_addEditNovel_to_home)
                    viewModel.navigateToHomeDone()
                }

            }


        }
    }


    private fun setViews() {
        binding.apply {

            updateViews()

            setAdapter()

            btnSubmit.setOnClickListener {
                mtitle = title.text?.trim().toString()
                mDescription = description.text?.trim().toString()
                mtype = type.text?.trim().toString()
                mWriter = writerName.text?.trim().toString()


                if (mtitle.isEmpty()){
                    title.error = "title required"
                    title.requestFocus()
                    return@setOnClickListener
                }
                if (mDescription.isEmpty()){
                    description.error = "description required"
                    description.requestFocus()
                    return@setOnClickListener
                }
                if (mWriter.isEmpty()){
                    writerName.error = "writer required"
                    writerName.requestFocus()
                    return@setOnClickListener
                }
                if (mtype.isEmpty()) {
                    type.error = "type required"
                    type.requestFocus()
                    return@setOnClickListener
                }
                if (mCategory.isEmpty()) {
                    btnCategory.error = "category required"
                    btnCategory.requestFocus()
                    return@setOnClickListener
                }
                if (mCover.isEmpty()) {
                    showMessage("cover required")
                }
                else if(mPdfs.isEmpty()) {
                    showMessage("please select at lest one pdf file")
                }
                else {
                    submit()
                }

            }

            /** button add cover **/
            btnAddCover.setOnClickListener {
                imagePicker.pickFromStorage { imageResult ->
                    imageCallBack(imageResult)
                }
            }

            /** button add pdf **/
            btnAddPdf.setOnClickListener {
                pdfPickIntent()
            }

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            /** button add category **/
            btnCategory.setOnClickListener {
                selectCategory(it)
            }

            btnDelete.setOnClickListener {
                showMessage("delete feature under development")
            }



        }
    }

    private fun updateViews() {
        binding.addEditNovelViewModel = viewModel
        binding.lifecycleOwner = this@AddEditNovel
    }



    private fun submit() {
        if (!isEdit){ // add new novel
            val novel = NovelData(getNovelId(),mtitle,mDescription,mtype,mCategory,mWriter,mCover)
            viewModel.uploadNovel(novel,mPdfs)
        }else { // update old novel
//            viewModel.updateNovel(novel!!)
            showMessage("update feature under development")
        }
    }


    // todo: the categories values with translations inside categories menu file.

    private fun selectCategory(v: View) {
        val popupMenu = PopupMenu(requireContext(),v)
        popupMenu.menuInflater.inflate(R.menu.categories_menu,popupMenu.menu)
//        popupMenu.setForceShowIcon(true) // this require android 10 and above
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.item_category1-> {
                    mCategory = NovelCategories.CATEGORY1.name
                    binding.btnCategory.text = mCategory
                }
                R.id.item_category2-> {
                    mCategory = NovelCategories.CATEGORY2.name
                    binding.btnCategory.text = mCategory
                }
                R.id.item_category3-> {
                    mCategory = NovelCategories.CATEGORY3.name
                    binding.btnCategory.text = mCategory
                }
                R.id.item_category4-> {
                    mCategory = NovelCategories.CATEGORY4.name
                    binding.btnCategory.text = mCategory
                }
                R.id.item_category5-> {
                    mCategory = NovelCategories.CATEGORY5.name
                    binding.btnCategory.text = mCategory
                }

            }


            true
        }
        popupMenu.show()
    }


    private fun setAdapter() {

        /** click listener **/
        pdfAdapter.clickListener = object: AdapterPdf.PdfClickListener {

            override fun onRemove(pdf: String, index: Int) {
                showMessage("under working")
//                val pdfs = novel?.pdfs?.toMutableList()
//                pdfs?.remove(pdf)
//                pdfAdapter.submitList(pdfs?.toList())
//                pdfAdapter.notifyItemRemoved(index)
            }

        }
        binding.rvPdfs.adapter = pdfAdapter
    }



    private fun imageCallBack(imageResult: ImageResult<Uri>) {
        when (imageResult) {
            is ImageResult.Success -> {
                val uri = imageResult.value
                mCover = uri.toString()
                setImageToCover(uri)
            }
            is ImageResult.Failure -> {
                val error = imageResult.errorString
            }
        }
    }

    private fun pdfPickIntent() {
        val intent= Intent()
        intent.type="application/pdf"
        intent.action= Intent.ACTION_GET_CONTENT
        pdfActivityResultLauncher.launch(intent)
    }


    private val pdfActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            pdfFile = result.data!!.data
            mPdfs.add(pdfFile!!)
            pdfAdapter.submitList(mPdfs.map { it.toString() })

//            pdfFiles.add(result.data!!.data!!)
            showMessage("pdf is ready")

        } else {
            // cancel picked.
        }
    }

    private fun setImageToCover(uri: Uri) {
        novel?.cover = uri.toString()
        binding.iconIv.setImageURI(uri)
    }


    private fun checkPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
               Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {}
                override fun onPermissionRationaleShouldBeShown(list: MutableList<PermissionRequest>?,
                    token: PermissionToken?) {
                }
            }).check()
    }

//    private fun addPdfSheet() {
//
//        bottomSheetPdf.setContentView(R.layout.bottom_add_pdf_sheet)
//        val submitBtn = bottomSheetPdf.findViewById<Button>(R.id.btnSubmitPdf)
//        val btnAttachPdf = bottomSheetPdf.findViewById<ImageView>(R.id.btnAttachPdf)
//        val pdfTitle = bottomSheetPdf.findViewById<TextInputEditText>(R.id.pdfTitle)
//
//        /** button attach pdf **/
//        btnAttachPdf?.setOnClickListener {
//            pdfPickIntent()
//        }
//
//        /** button submit pdf **/
//        submitBtn?.setOnClickListener {
//            val title = pdfTitle?.text?.trim().toString()
//            if (title.isNotEmpty()) {
//                val pdf = PdfData(getPdfId(),pdfTitle?.text?.trim().toString(),pdfFile.toString(),novel!!.novelId)
//                val mPdfs = novel?.pdfs?.toMutableList()
//                    mPdfs?.add(pdf)
//                    pdfAdapter.submitList(mPdfs)
//                    novel!!.pdfs = mPdfs!!
//
//            }
//        }
//        bottomSheetPdf.show()
//
//    }




    fun setData() {
        isEdit = arguments?.get(Constants.IS_EDIT_KEY) as Boolean
        novel = try { arguments?.get(Constants.NOVEL_KEY) as NovelData } catch (ex: Exception){ null }
        viewModel.novel.value = novel
        viewModel.isEdit.value = isEdit
        if (novel != null){
            mCategory = novel!!.category
            mCover = novel!!.cover
            val x = novel!!.pdfs.map { it.toUri() } as ArrayList
            mPdfs = x
            pdfAdapter.submitList(novel!!.pdfs)
        }
    }



}