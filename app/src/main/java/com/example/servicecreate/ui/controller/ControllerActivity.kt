package com.example.servicecreate.ui.controller

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.base.activity.BaseActivity
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ActivityControllerBinding
import com.example.servicecreate.ui.controller.air.AirConditionFragment
import com.example.servicecreate.ui.controller.light.LightFragment


class ControllerActivity :BaseActivity<ActivityControllerBinding>() {

    private lateinit var airFragment: AirConditionFragment
    private lateinit var lightFragment: LightFragment

    private var type: Int = 0;

    override fun ActivityControllerBinding.initBindingView() {
        type = intent.getIntExtra("type", 0)

        initFragment()

        when(type){
            1 -> setCurrentFragment(airFragment)
            2 -> setCurrentFragment(lightFragment)
        }

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
        lightFragment = LightFragment()
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
        fun startActivity(context: Context, type: Int){
            val intent = Intent(context, ControllerActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

}