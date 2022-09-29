package com.shaimaziyad.khayal.screens.viewPdf

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.storage.FirebaseStorage
import com.shaimaziyad.khayal.utils.Constants
import com.shaimaziyad.khayal.databinding.ViewPdfBinding

class ViewPdf : Fragment() {

    private lateinit var binding : ViewPdfBinding

    private var novelUri = ""
    private var url = ""

//    private lateinit var mAdView: AdView
//
//    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = ViewPdfBinding.inflate(layoutInflater)

//        try {
//            novelUri = arguments?.get(Constants.KEY_NOVEL_URI).toString()
//            loadNovelFromUrl(novelUri)
//        }catch (ex: Exception){}

        return binding.root
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

        //handle click go back
        binding.btnBack.setOnClickListener {
//            showMInterstitialAd()

        }
        /** button back **/
       binding.btnBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun loadNovelFromUrl(url: String){

        Log.d(TAG, "loadNovelFromUrl: Get Pdf from firebase storage using URL")

        val reference =  FirebaseStorage.getInstance().getReferenceFromUrl(url).getBytes(Constants.MAX_BYTES_PDF)
        reference.addOnSuccessListener { bytes->
            Log.d(TAG, "loadNovelFromUrl: pdf got from url")

            //load pdf
            binding.pdfView.fromBytes(bytes)
                .swipeHorizontal(false)//set false to scroll vertical, set tru to scroll horizontal
                .onPageChange { page, pageCount ->
                    //set current and total pages in toolbar subtitle
                    val currentPage = page+1 //page starts from 0 so do +1 to start from 1
                    binding.toolbarSubTitleTv.text = "$currentPage/$pageCount"
                    Log.d(TAG, "loadNovelFromUrl: $currentPage/$pageCount")
                }
                .onError { t->
                    Log.d(TAG, "loadNovelFromUrl: Bug ne ${t.message}")
                }
                .onPageError { page, t ->
                    Log.d(TAG, "loadNovelFromUrl: Bug ne ${t.message}")
                }
                .load()
            binding.progressBar.visibility = View.GONE

        }
            .addOnFailureListener { e->
                Log.d(TAG, "loadNovelFromUrl: Failed to get pdf due to ${e.message}")
                binding.progressBar.visibility = View.GONE
            }
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
