package com.example.servicecreate.ui.controller

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.base.activity.BaseActivity
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ActivityControllerBinding
import com.example.servicecreate.ui.controller.air.AirConditionFragment
import com.example.servicecreate.ui.controller.doorlock.DoorLockFragment
import com.example.servicecreate.ui.controller.light.LightFragment
import com.example.servicecreate.ui.home.home.HomeViewModel


class ControllerActivity :BaseActivity<ActivityControllerBinding>() {

    private lateinit var airFragment: AirConditionFragment
    private lateinit var lightFragment: LightFragment
    private lateinit var doorLockFragment: DoorLockFragment

    internal val mViewModel: ControllerViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[ControllerViewModel::class.java]
    }

    override fun ActivityControllerBinding.initBindingView() {
        viewModel = mViewModel

        mViewModel.apply {
            id = intent.getLongExtra("id", 0L)
            type = intent.getIntExtra("type", 0)
            name = intent.getStringExtra("name").toString()
            roomName = intent.getStringExtra("roomName").toString()
        }

        initFragment()

        when(mViewModel.type){
            1 -> setCurrentFragment(airFragment)
            2 -> setCurrentFragment(lightFragment)
            3 -> setCurrentFragment(doorLockFragment)
        }

        controllerToolbar.apply {
            title = mViewModel.name
            subtitle = mViewModel.roomName
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
        doorLockFragment = DoorLockFragment()
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
        fun startActivity(context: Context, type: Int, id: Long, name: String, name1: String){
            val intent = Intent(context, ControllerActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("roomName", name1)
            context.startActivity(intent)
        }
    }

}