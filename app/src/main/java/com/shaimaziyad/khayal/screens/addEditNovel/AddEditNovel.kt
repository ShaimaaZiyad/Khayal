package com.shaimaziyad.khayal.screens.addEditNovel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.data.PdfData
import com.shaimaziyad.khayal.databinding.AddEditNovelBinding
import com.shaimaziyad.khayal.utils.Constants


class AddEditNovel : Fragment() {

    private lateinit var binding: AddEditNovelBinding
    private var isEdit = false
    private var novel: NovelData? = null
    private val pdfAdapter by lazy { AdapterPdf() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = AddEditNovelBinding.inflate(layoutInflater)

        setViews()
        setData()
        setDataInViews()


        return binding.root
    }

    private fun setDataInViews() {
        binding.novelData = novel
        binding.lifecycleOwner = this
    }



    private fun setAdapter() {
        pdfAdapter.clickListener = object: AdapterPdf.PdfClickListener{

            override fun onRemove(pdf: PdfData, index: Int) {
                val pdfs = novel?.pdfs?.toMutableList()
                pdfs?.remove(pdf)
                pdfAdapter.submitList(pdfs?.toList())
                pdfAdapter.notifyItemRemoved(index)
            }

        }
        binding.rvPdfs.adapter = pdfAdapter
    }


    private fun setViews() {
        binding.apply {


            setAdapter()

            submitBtn.setOnClickListener {
                Toast.makeText(requireContext(),"send data success", Toast.LENGTH_SHORT).show()
            }

            attachCoverBtn.setOnClickListener {
                Toast.makeText(requireContext(),"send data success", Toast.LENGTH_SHORT).show()
            }

            addPdf.setOnClickListener {
                showBottomSheet()
            }

            /** button back **/
            backBtn.setOnClickListener{
                findNavController().navigateUp()
            }
        }
    }
    // todo add bottom sheet for add pdf.

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.bottom_add_pdf_sheet)
        val submitBtn = dialog.findViewById<Button>(R.id.submitBtn)

        submitBtn?.setOnClickListener {
            // send pdf file
            Toast.makeText(requireContext() , " تم الارسال بنجاح", Toast.LENGTH_SHORT).show()

        }
        dialog.show()
    }




    fun setData(){
        isEdit = arguments?.get(Constants.IS_EDIT_KEY) as Boolean
        novel = try { arguments?.get(Constants.NOVEL_KEY) as NovelData } catch (ex: Exception){ null }
    }

}