package com.shaimaziyad.khayal.screens.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.Novel
import com.shaimaziyad.khayal.databinding.SearchBinding
import com.shaimaziyad.khayal.screens.home.HomeViewModel
import com.shaimaziyad.khayal.screens.profile.ProfileViewModel
import com.shaimaziyad.khayal.sheets.FilterNovelSheet
import com.shaimaziyad.khayal.utils.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class Search : Fragment() {

    private val homeViewModel by sharedViewModel<HomeViewModel>()
    private val profileViewModel by sharedViewModel<ProfileViewModel>()

    private lateinit var binding: SearchBinding

    private lateinit var filterSheet: FilterNovelSheet
    private val searchAdapter by lazy { SearchAdapter() }
    private lateinit var filteredCategory: List<Novel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchBinding.inflate(inflater, container, false)
        filterSheet = FilterNovelSheet(binding.filterSheet, this)

        setData()
        setViews()
        setObserves()


        return binding.root
    }

    private fun setData() {
        filteredCategory = try {
            arguments?.getSerializable(Constants.CATEGORY_KEY) as List<Novel>
        } catch (ex: Exception) {
            emptyList()
        }
    }

    private fun setViews() {
        setToolBar()
        setAdapter()
    }


    private fun setObserves() {
        val novels = homeViewModel.novels.value
        if (filteredCategory.isNotEmpty()) {
            searchAdapter.submitList(filteredCategory)
        } else {
            if (!novels.isNullOrEmpty()) {
                binding.noDataLayout.hide()
                searchAdapter.submitList(novels)
            } else {
                binding.noDataLayout.show()
            }
        }
    }


    private fun setAdapter() {
        searchAdapter.clickListener = object : SearchAdapter.ClickListener {
            override fun onClick(novel: Novel, index: Int) {
                val userType = profileViewModel.user.value?.userType
                if (userType == UserType.USER.name) {
                    navigateToNovelDetails(novel)
                } else {
                    navigateToAddEditNovel(isEdit = true, novel)
                }
            }
        }
        binding.rvNovels.adapter = searchAdapter
    }


    private fun navigateToNovelDetails(novel: Novel) {
        val data = bundleOf(Constants.NOVEL_KEY to novel)
        findNavController().navigate(R.id.action_search_to_novelDetails, data)
    }

    private fun navigateToAddEditNovel(isEdit: Boolean, novel: Novel) {
        val data = bundleOf(Constants.NOVEL_KEY to novel, Constants.IS_EDIT_KEY to isEdit)
        findNavController().navigate(R.id.action_search_to_addEditNovel, data)
    }


    private fun setToolBar() {
        binding.searchLayout.apply {

            setFilterStatus()

            /** button back **/
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            /** button filter **/
            filter.btnFilter.setOnClickListener {
                if (filteredCategory.isNotEmpty()) {
                    filterSheet.novels = filteredCategory
                } else {
                    filterSheet.novels = homeViewModel.novels.value!!
                }
                filterSheet.showSheet()
            }

            /** button clear search **/
            btnClearSearch.setOnClickListener {
                search.setText("")
                filterSheet.clearFilter()
            }


            /** search listener **/
            search.doAfterTextChanged {
                val query = it?.trim().toString()
                if (query.isNotEmpty()) {
                    btnClearSearch.show()
                    binding.noDataLayout.hide()
                    if (filteredCategory.isNotEmpty()) {
                        val search = filteredCategory.filter {
                            it.title.lowercase().contains(query.lowercase())
                        }
                        searchAdapter.submitList(search)
                    } else {
                        val search = homeViewModel.searchByNovelTitle(query)
                        searchAdapter.submitList(search)
                    }
                } else {
                    btnClearSearch.hide()
                    if (filteredCategory.isNotEmpty()) {
                        searchAdapter.submitList(filteredCategory)
                    } else {
                        val search = homeViewModel.novels.value!!
                        searchAdapter.submitList(search)
                    }
                }
            }
        }
    }


    private fun setFilterStatus() {
        filterSheet.filterStatus = object : FilterNovelSheet.FilterStatus {

            override fun onFilter(filtered: List<Novel>) {
                binding.searchLayout.filter.badgeNumber.show()
                binding.searchLayout.filter.badgeNumber.text = filtered.size.toString()
                searchAdapter.submitList(filtered)
            }

            override fun clearFilter() {
                binding.searchLayout.filter.badgeNumber.hide()
                if (filteredCategory.isNotEmpty()) {
                    searchAdapter.submitList(filteredCategory)
                } else {
                    searchAdapter.submitList(homeViewModel.novels.value ?: emptyList())
                }

            }


        }
    }


}