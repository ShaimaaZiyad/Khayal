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


class PdfViewer : Fragment() {

    private lateinit var binding : PdfViewerBinding
    private lateinit var viewModel: PdfViewerViewModel

    private var novelUri = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = PdfViewerBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[PdfViewerViewModel::class.java]


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        MobileAds.initialize(this@ViewPdf.requireContext()) {}
//
//        mAdView = binding.adView
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
//
//        loadMInterstitialAd()


    }

//    private fun loadNovelFromUrl(url: String) {
//
//        Log.d(TAG, "loadNovelFromUrl: Get Pdf from firebase storage using URL")
//
//        val reference =  FirebaseStorage.getInstance().getReferenceFromUrl(url).getBytes(Constants.MAX_BYTES_PDF)
//        reference.addOnSuccessListener { bytes->
//            Log.d(TAG, "loadNovelFromUrl: pdf got from url")
//
//            //load pdf
//            binding.pdfView.fromBytes(bytes)
//                .swipeHorizontal(false)//set false to scroll vertical, set tru to scroll horizontal
//                .onPageChange { page, pageCount ->
//                    //set current and total pages in toolbar subtitle
//                    val currentPage = page+1 //page starts from 0 so do +1 to start from 1
//                    binding.toolbarSubTitleTv.text = "$currentPage/$pageCount"
//                    Log.d(TAG, "loadNovelFromUrl: $currentPage/$pageCount")
//                }
//                .onError { t->
//                    Log.d(TAG, "loadNovelFromUrl: Bug ne ${t.message}")
//                }
//                .onPageError { page, t ->
//                    Log.d(TAG, "loadNovelFromUrl: Bug ne ${t.message}")
//                }
//                .load()
//            binding.progressBar.visibility = View.GONE
//
//        }
//            .addOnFailureListener { e->
//                Log.d(TAG, "loadNovelFromUrl: Failed to get pdf due to ${e.message}")
//                binding.progressBar.visibility = View.GONE
//            }
//    }

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
