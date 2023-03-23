package com.example.servicecreate

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.base.activity.BaseActivity
import com.example.base.kxt.toast
import com.example.servicecreate.databinding.ActivityMainBinding
import com.example.servicecreate.ui.append.AppendFragment
import com.example.servicecreate.ui.auth.AuthActivity
import com.example.servicecreate.ui.home.HomeFragment
import com.example.servicecreate.ui.setting.SettingFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var settingFragment: SettingFragment
    private lateinit var homeFragment: HomeFragment

    override fun ActivityMainBinding.initBindingView() {

        initFragment()
        initUserData()

        setCurrentFragment(homeFragment)

        floatButton.setOnClickListener {
            AppendFragment().show(supportFragmentManager, "Append")
        }

        bottomNavigationView.apply {
            background = null
            menu.getItem(1).isEnabled = false
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.nav_home ->{
                        setCurrentFragment(homeFragment)
                        true
                    }
                    R.id.nav_setting ->{
                        setCurrentFragment(settingFragment)
                        true
                    }
                    else -> true
                }
            }
        }
    }

    /**
     * Verify Login Status
     */
    private fun initUserData() {
        if(!ServiceCreateApplication.sp.getBoolean(ServiceCreateApplication.isLogin, false)){
            toast(R.string.home_login_status_warning)
            AuthActivity.startActivity(this@MainActivity)
            finish()
        }
    }

    /*
    Init all fragment
     */
    private fun initFragment(){
        settingFragment = SettingFragment()
        homeFragment = HomeFragment()
    }

    /*
    Set this activity`s fragment
     */
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_fragment, fragment)
            commit()
        }

    /*
    To launch this activity
     */
    companion object{
        fun startActivity(context: Context){
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}