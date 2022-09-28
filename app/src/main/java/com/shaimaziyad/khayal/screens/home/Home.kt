package com.shaimaziyad.khayal.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shaimaziyad.khayal.R
import com.shaimaziyad.khayal.data.NovelData
import com.shaimaziyad.khayal.databinding.HomeBinding
import com.shaimaziyad.khayal.utils.Constants
import com.shaimaziyad.khayal.utils.UserType

class Home : Fragment() {

    private lateinit var binding: HomeBinding

    private lateinit var viewModel: HomeViewModel

    private val novelAdapter by lazy { AdapterNovel() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = HomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        setViews()
        setObserves()

        return binding.root
    }


    override fun onResume() {
        super.onResume()

//        viewModel.loadUser()

    }

    private fun setObserves() {
        viewModel.apply {

            /** novels live data **/
            novels.observe(viewLifecycleOwner){ novels->
                if (!novels.isNullOrEmpty()){ // novel is exist.

                    novelAdapter.submitList(novels)
                }
            }
        }
    }


    private fun setAdapter(){

        novelAdapter.clickListener = object: AdapterNovel.NovelClickListener {

            // todo: check if user is admin or normal user then go to specific screen.
            override fun onClick(novel: NovelData, index: Int) {
                val userType = viewModel.user.value?.userType

                if (UserType.USER.name == userType) { // user
                    val data = bundleOf(Constants.NOVEL_KEY to novel)
                    findNavController().navigate(R.id.action_userHome_to_novelDetails,data)

                }else{ // admin
                    // we can set true for is edit bundle if we need to edit the novel only.
                    val data = bundleOf(Constants.NOVEL_KEY to novel,Constants.IS_EDIT_KEY to true)
                    findNavController().navigate(R.id.action_home_to_addEditNovel,data)
                }
            }

        }
        binding.novelsRv.adapter = novelAdapter
    }

    private fun setViews() {
        binding.apply {

            homeViewModel = viewModel
            lifecycleOwner = this@Home

            setAdapter()

            /** button sign out **/
            logoutBtn.setOnClickListener {
//                findNavController().navigateUp()

            }

            /** button profile **/
            profileBtn.setOnClickListener {
                findNavController().navigate(R.id.action_userHome_to_profile)
            }
        }
    }
}