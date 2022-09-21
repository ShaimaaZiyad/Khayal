//package com.shaimaziyad.khayal.screens
//
//import android.app.Activity
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.navigation.NavController
//import androidx.navigation.Navigation
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.NavigationUI.setupWithNavController
//import com.shaimaziyad.khayal.R
//import com.shaimaziyad.khayal.databinding.AdminControlBinding
//
//
//class AdminControl : Fragment() {
//
//    private lateinit var binding: AdminControlBinding
//
//    private lateinit var navController : NavController
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = AdminControlBinding.inflate(layoutInflater)
//
//        setViews()
//
//        return binding.root
//    }
//    private fun setViews(){
//        binding.apply {
//           profileBtn.setOnClickListener {
//                findNavController().navigate(R.id.action_adminControl_to_profile)
//            }
//            logoutBtn.setOnClickListener {
//                requireActivity().finish()
//            }
//
//            navController = Navigation.findNavController(requireActivity(),R.id.admin_nav_host)
//            setupWithNavController(binding.bottomNavigationView,navController)
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }
//
//}