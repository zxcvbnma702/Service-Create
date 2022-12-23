package com.example.servicecreate

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.base.activity.BaseActivity
import com.example.servicecreate.databinding.ActivityMainBinding
import com.example.servicecreate.ui.device.DeviceFragment
import com.example.servicecreate.ui.personage.PersonageFragment
import com.example.servicecreate.ui.scene.SceneFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var personageFragment: PersonageFragment
    private lateinit var sceneFragment: SceneFragment
    private lateinit var deviceFragment: DeviceFragment

    override fun ActivityMainBinding.initBindingView() {
        initFragment()

        setCurrentFragment(deviceFragment)

        bottomNavigationView.apply {
            background = null
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.nav_home ->{
                        setCurrentFragment(deviceFragment)
                        true
                    }
                    R.id.nav_message ->{
                        setCurrentFragment(sceneFragment)
                        true
                    }
                    R.id.nav_person ->{
                        setCurrentFragment(personageFragment)
                        true
                    }
                    else -> true
                }
            }
        }
    }

    private fun initFragment(){
        personageFragment = PersonageFragment()
        sceneFragment = SceneFragment()
        deviceFragment = DeviceFragment()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_fragment, fragment)
            commit()
        }

    companion object{
        fun startActivity(context: Context){
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}