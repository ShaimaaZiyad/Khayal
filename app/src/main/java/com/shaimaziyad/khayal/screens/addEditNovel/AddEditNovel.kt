package com.shaimaziyad.khayal.screens.addEditNovel

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Novel
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import com.karumi.dexter.listener.PermissionRequest
import com.shaimaziyad.khayal.databinding.AddEditNovelBinding
import com.shaimaziyad.khayal.screens.home.HomeViewModel
import com.shaimaziyad.khayal.utils.*
import com.shaimaziyad.khayal.utils.getNovelId
import org.koin.android.viewmodel.ext.android.sharedViewModel


class AddEditNovel : Fragment() {

    companion object {
        const val TAG = "AddEditNovel"
    }

    private lateinit var binding: AddEditNovelBinding

    private val viewModel by sharedViewModel<AddEditNovelViewModel>()
    private val homeViewModel by sharedViewModel<HomeViewModel>()

    private var isEdit = false

    private var novel: Novel? = null
    private var mtitle = ""
    private var mDescription = ""
    private var mWriter = ""
    private var mType = ""
    private var mCategory = ""
    private var mCover = ""
    private var mPdfs = ArrayList<Uri>()


    private val pdfAdapter by lazy { PdfAdapter() }
    private var pdfFile: Uri? = null
    private lateinit var bottomSheetPdf: BottomSheetDialog
    private lateinit var imagePicker: ImagePicker
    private lateinit var novelFilter: NovelFilter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddEditNovelBinding.inflate(layoutInflater)
//        viewModel = ViewModelProvider(this)[AddEditNovelViewModel::class.java]
        imagePicker = ImagePicker(this)
        bottomSheetPdf = BottomSheetDialog(requireContext())
        novelFilter = NovelFilter(requireContext())

        setData()
        setViews()
        checkPermission()
        setObserves()


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // set the lists
        setListToAutoComplete(requireContext(),novelFilter.novelCategories,binding.btnSelectCategory) // set categories list
        setListToAutoComplete(requireContext(),novelFilter.novelType,binding.btnSelectType) // set types list

    }


    private fun setObserves() {
        viewModel.apply {

            /** navigate to home live data **/
            navigateToHome.observe(viewLifecycleOwner) {
                if (it == true){
                    findNavController().navigate(R.id.action_addEditNovel_to_home)
                    homeViewModel.loadNovels()
                    viewModel.navigateToHomeDone()
                }
            }




            /** info live data **/
            info.observe(viewLifecycleOwner){ info ->
                if (info != null){
                    showMessage(info)
                    viewModel.resetStatus()
                }
            }


        }
    }


    private fun setViews() {
        binding.apply {


            updateViews()

            setAdapter()

            /** button submit **/
            btnSubmit.setOnClickListener {
                mtitle = title.text?.trim().toString()
                mDescription = description.text?.trim().toString()
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
                if (mType.isEmpty()) {
                    showMessage("type required")
                }
                if (mCategory.isEmpty()) {
                    showMessage("category required")
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


            /** select category **/
            btnSelectCategory.setOnItemClickListener { adapterView, view, index, l ->
                mCategory = index.toString()
            }

            /** select type **/
            btnSelectType.setOnItemClickListener { adapterView, view, index, l ->
                mType = index.toString()
            }


            /** button delete **/
            btnDelete.setOnClickListener {
                viewModel.deleteNovel(novel!!)
            }



        }
    }

    private fun updateViews() {
        binding.addEditNovelViewModel = viewModel
        binding.lifecycleOwner = this@AddEditNovel
    }



    private fun submit() {
        if (!isEdit){ // add new novel
            val novel = Novel(getNovelId(),mtitle,mDescription,mType,mCategory,mWriter,mCover)
            viewModel.uploadNovel(novel,mPdfs)
            Log.d(TAG,"newUri: $mCover")
        }else { // update old novel
            novel?.title = mtitle
            novel?.description = mDescription
            novel?.type = mType
            novel?.category = mCategory
            novel?.writer = mWriter
            novel?.cover = mCover
            Log.d(TAG,"oldUri: $mCover")
            viewModel.updateNovel(novel!!,mPdfs)

        }
    }


    private fun setAdapter() {

        /** click listener **/
        pdfAdapter.clickListener = object: PdfAdapter.PdfClickListener {

            override fun onRemove(pdf: String, index: Int) {
//                val pdfs = novel?.pdfs?.toMutableList()
                val pdfs = mPdfs.toMutableList().map { it.toString() } as ArrayList
                pdfs?.remove(pdf)
                mPdfs = pdfs?.map { it.toUri() } as ArrayList

                pdfAdapter.submitList(pdfs)

//                pdfAdapter.notifyDataSetChanged()

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
        binding.btnAddCover.setImageURI(uri)
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


    private fun setData() {
        isEdit = arguments?.get(Constants.IS_EDIT_KEY) as Boolean
        novel = try { arguments?.get(Constants.NOVEL_KEY) as Novel } catch (ex: Exception){ null }
        viewModel.novel.value = novel
        viewModel.isEdit.value = isEdit
        if (novel != null){
            mCategory = novel!!.category
            mType = novel!!.type
            mCover = novel!!.cover
            mPdfs = novel!!.pdfs.map { it.toUri() } as ArrayList
            pdfAdapter.submitList(novel!!.pdfs)



        }

    }



}