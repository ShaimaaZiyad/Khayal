package com.shaimaziyad.khayal1.screens.novelDetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.shaimaziyad.khayal1.R
import com.shaimaziyad.khayal1.data.Novel
import com.shaimaziyad.khayal1.databinding.NovelDetailsBinding
import com.shaimaziyad.khayal1.screens.home.HomeViewModel
import com.shaimaziyad.khayal1.screens.home.NovelAdapter
import com.shaimaziyad.khayal1.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal1.utils.*

import org.koin.android.viewmodel.ext.android.sharedViewModel

@SuppressLint("NotifyDataSetChanged")
class NovelDetails : Fragment() {

    private lateinit var binding: NovelDetailsBinding

    private var novel: Novel? = null
    private lateinit var novelDetailsAdapter: NovelDetailsAdapter
    private val homeViewModel by sharedViewModel<HomeViewModel>()
    private val profileViewModel by sharedViewModel<ProfileViewModel>()

    private val novelAdapter by lazy { NovelAdapter() }
    private val chapterAdapter by lazy { ChapterAdapter() }

    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "NovelDetails"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = NovelDetailsBinding.inflate(layoutInflater)
        novelDetailsAdapter = NovelDetailsAdapter(this)

        MobileAds.initialize(requireContext()) {}

        setInterstitialAd()
        setData()
        setViews()
        setObserves()

        return binding.root
    }

    private fun setInterstitialAd() {
        val adRequest2 = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),getString(R.string.ID_Interstitial), adRequest2, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    private fun setViews() {
        binding.apply {

            novelData = novel
            lifecycleOwner = this@NovelDetails

            setChapterAdapter()
            setNovelAdapter()
            setNovelDetails()
            setLikeStatus()
            setAd()

            /** button like **/
            btnLike.setOnClickListener {
                profileViewModel.setLikeToNovel(novel!!)
            }

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }


            /** button share **/
            btnShare.setOnClickListener {
                // todo add share feature
                showMessage("share")
            }


        }
    }

    private fun setAd() {
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }


    private fun setObserves() {
        profileViewModel.apply {
            homeViewModel.apply {

                /** novels live data **/
                novels.observe(viewLifecycleOwner) {
                    if (!it.isNullOrEmpty()) {
                        val suggestions =
                            it.filter { it.category == novel?.category && it.novelId != novel!!.novelId }
                        if (suggestions.isEmpty()) {
                            binding.suggestionsLayout.root.hide()
                        }
                        novelAdapter.submitList(suggestions)
                    }
                }

                /** like status **/
                likeStatus.observe(viewLifecycleOwner) {
                    when (it) {
                        DataStatus.LOADING -> {
                            binding.btnLike.isClickable = false
                        }
                        DataStatus.SUCCESS -> {
                            binding.btnLike.isClickable = true
                        }
                        DataStatus.ERROR -> {
                            binding.btnLike.isClickable = true
                        }
                        else -> {}
                    }
                }


            }
        }
    }


    private fun setLikeStatus() {
        binding.btnLike.isChecked = profileViewModel.isNovelLiked(novel!!.novelId)!!
    }

    private fun setData() {
        novel = try {
            arguments?.get(Constants.NOVEL_KEY) as Novel
        } catch (ex: Exception) {
            null
        }
    }


    private fun setChapterAdapter() {
        val data = novel?.pdfs
        chapterAdapter.novelCover = novel!!.cover
        chapterAdapter.clickListener = object : ChapterAdapter.ClickListener {
            override fun onClick(uri: String) {
                navigateToPdfViewer(uri)
            }
        }
        chapterAdapter.submitList(data)
        binding.rvChapters.apply {
            adapter = chapterAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(
                binding.rvChapters.context,
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )
        }

    }

    private fun setNovelAdapter() {
        binding.suggestionsLayout.tvCategory.text = "Suggestions"

        /** button show suggestions **/
        binding.suggestionsLayout.btnShowCategory.setOnClickListener {

        }

        /** click listener adapter **/
        novelAdapter.clickListener = object : NovelAdapter.ClickListener {
            override fun onClick(novel: Novel, index: Int) {
                navigateToSelf(novel)
            }
        }

        /** suggestions layout **/
        binding.suggestionsLayout.rvNovels.apply {
            adapter = novelAdapter
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(
                binding.suggestionsLayout.rvNovels.context,
                1,
                GridLayoutManager.HORIZONTAL,
                false
            )
        }


    }


    private fun setNovelDetails() {
        binding.novelDetailsLayout.novel = novel
    }

    fun navigateToSelf(novel: Novel) {
        val data = bundleOf(Constants.NOVEL_KEY to novel)
        findNavController().navigate(R.id.action_novelDetails_self, data)
    }

    fun navigateToPdfViewer(pdf: String) {
        val data = bundleOf(Constants.PDF_KEY to pdf)
        findNavController().navigate(R.id.action_novelDetails_to_pdfViewer, data)
        showInterstitialAd()

    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }


}