package com.example.ccl3_bbetter_app.onBoarding.Screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ccl3_bbetter_app.MainActivity2
import com.example.ccl3_bbetter_app.R

class splashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Handler().postDelayed({

            if(onBoardingFinished()){
                //findNavController().navigate(R.id.action_splashFragment_to_mainActivity2)
                val intent = Intent(context, MainActivity2::class.java)
                startActivity(intent)
            }else{
                findNavController().navigate(R.id.action_splashScreen_to_viewPagerFragment)
            }
        }, 3000)
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)

    }

    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}