package com.example.servicecreate.ui.auth

import android.animation.LayoutTransition
import android.animation.TimeInterpolator
import android.transition.Transition
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import com.example.base.activity.BaseActivity
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayout


class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override fun ActivityAuthBinding.initBindingView() {
        val transition = LayoutTransition()
        transition.setInterpolator(Transition.MATCH_ID, LinearInterpolator())
        authConstraint.layoutTransition = transition

        authTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab?.text?.equals(getString(R.string.auth_tab1)) == true) {
                    authEdVerify.visibility = View.GONE
                    getString(R.string.auth_tab1).also { authBtLogin.text = it }
                } else {
                    authEdVerify.visibility = View.VISIBLE
                    authBtLogin.text = getString(R.string.auth_tab2)
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }

}