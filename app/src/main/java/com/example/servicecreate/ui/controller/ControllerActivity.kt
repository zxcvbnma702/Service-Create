package com.example.servicecreate.ui.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.base.activity.BaseActivity
import com.example.servicecreate.MainActivity
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ActivityControllerBinding
import com.example.servicecreate.ui.controller.air.AirConditionFragment
import com.example.servicecreate.ui.home.HomeFragment
import com.example.servicecreate.ui.setting.SettingFragment


class ControllerActivity :BaseActivity<ActivityControllerBinding>() {

    private lateinit var airFragment: AirConditionFragment

    override fun ActivityControllerBinding.initBindingView() {
        initFragment()

        setCurrentFragment(airFragment)

        controllerToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    /*
   Init all fragment
    */
    private fun initFragment(){
       airFragment = AirConditionFragment()
    }

    /*
    Set this activity`s fragment
     */
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.controller_container_fragment, fragment)
            commit()
        }

    /*
    To launch this activity
     */
    companion object{
        fun startActivity(context: Context){
            val intent = Intent(context, ControllerActivity::class.java)
            context.startActivity(intent)
        }
    }

}