package com.shaimaziyad.khayal.screens.pdfViewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.databinding.PdfViewerBinding
import com.shaimaziyad.khayal.utils.Constants
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PdfViewer : Fragment() {

    private lateinit var binding : PdfViewerBinding
    private val viewModel by sharedViewModel<PdfViewerViewModel>()

    private var novelUri = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = PdfViewerBinding.inflate(layoutInflater)


        setData()

        setViews()
        setObserves()

        return binding.root
    }

    private fun setObserves() {

        viewModel.apply {

            /** pdf data **/
            pdf.observe(viewLifecycleOwner){ data ->
                if (data != null) {
                    binding.pdfView.fromBytes(data)
                        .swipeHorizontal(false)//set false to scroll vertical, set tru to scroll horizontal
                        .onPageChange { page, pageCount ->
                            //set current and total pages in toolbar subtitle
                            val currentPage = page + 1 //page starts from 0 so do +1 to start from 1
                            binding.toolbarSubTitleTv.text = "$currentPage/$pageCount"
                        }
                        .onError { error -> }
                        .onPageError { page, error -> }
                        .load()

                }
            }


        }
    }


    private fun setViews() {
        binding.apply {

            pdfViewerViewModel = viewModel
            lifecycleOwner = this@PdfViewer



            /** button back **/
            btnBack.setOnClickListener{
                findNavController().navigateUp()
            }




        }
    }



    private fun setData() {
        novelUri = try {arguments?.get(Constants.PDF_KEY).toString() }
        catch (ex: Exception){""}

        viewModel.loadPdf(novelUri)


    }




//    private fun showMInterstitialAd() {
//        if (mInterstitialAd != null) {
//            mInterstitialAd?.show(this@ViewPdf.requireActivity())
//        } else {
//            Log.d("TAG", "The interstitial ad wasn't ready yet.")
//        }
//    }
//
//    private fun loadMInterstitialAd() {
//        val adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(
//            this@ViewPdf.requireContext(),
//            getString(R.string.ID_Interstitial),
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
//                    mInterstitialAd = null
//                }
//
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    mInterstitialAd = interstitialAd
//                }
//            })
//    }

}
