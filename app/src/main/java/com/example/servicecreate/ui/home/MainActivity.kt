package com.example.servicecreate.ui.home

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.base.activity.BaseActivity
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.ActivityMainBinding
import com.example.servicecreate.ui.home.append.AppendFragment
import com.example.servicecreate.ui.auth.AuthActivity
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogOkInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.example.servicecreate.ui.home.exhibit.ExhibitFragment
import com.example.servicecreate.ui.home.gateway.GatewayFragment
import com.example.servicecreate.ui.home.home.HomeFragment
import com.example.servicecreate.ui.home.home.HomeViewModel
import com.example.servicecreate.ui.home.person.PersonFragment
import com.example.servicecreate.ui.home.setting.SettingFragment
import com.example.servicecreate.ui.home.watch.WatchFragment
import com.example.servicecreate.ui.home.wisdom.WisdomFragment
import com.example.servicecreate.ui.toast
import com.kongzue.dialogx.dialogs.MessageDialog
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var settingFragment: SettingFragment
    private lateinit var homeFragment: HomeFragment

    private val mViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]
    }

    override fun ActivityMainBinding.initBindingView() {

        initFragment()
        initUserData()

        viewModel = mViewModel

        setCurrentFragment(homeFragment)

//        floatButton.setOnClickListener {
//            AppendFragment(0).show(supportFragmentManager, "Append")
//        }

        bottomNavigationView.apply {
            background = null
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

        with(mViewModel){
            lifecycleScope.launch {
                mViewModel._jumpToExhibit.collect{
                   when(it){
                       0L -> "错误".toast()
                       else -> exhibitFragment(it, this@with.roomName)
                    }
                }
            }
            lifecycleScope.launch {
                mViewModel._jumpTogateWay.collect{
                    when(it){
                        1 -> gatewayFragment()
                    }
                }
            }
            lifecycleScope.launch {
                mViewModel._jumpToWisdom.collect{
                    when(it){
                        1 -> wisdomFragment()
                    }
                }
            }
            lifecycleScope.launch{
                mViewModel._jumpToPerson.collect{
                    when(it){
                        1 -> personFragment()
                    }
                }
            }

            lifecycleScope.launch{
                mViewModel._jumpToWatch.collect{
                    when(it){
                        1 -> watchFragment()
                    }
                }
            }
        }
    }

    /**
     * Verify Login Status
     */
    private fun initUserData() {

        if(!ServiceCreateApplication.sp.getBoolean(ServiceCreateApplication.isLogin, false)){
            MessageDialog.show(getString(R.string.auth_state_abnormal), getString(R.string.auth_state_abnormal_tip), "确定")
                .setMaskColor(getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setTitleTextInfo(dialogTitleInfo(this))
                .setOkTextInfo(dialogOkInfo(this))
                .setMessageTextInfo(dialogMessageInfo(this))
                .setOkButton { _, _ ->
                    AuthActivity.startActivity(this@MainActivity)
                    finish()
                    false
                    }
        }

        ServiceCreateApplication.appSecret =
            ServiceCreateApplication.sp.getString(ServiceCreateApplication.userID, "").toString()
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
            setReorderingAllowed(true)
            commit()
        }

    /**
     * Jump to ExhibitFragment
     */
    private fun exhibitFragment(l: Long, roomName: String) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.top_container, ExhibitFragment(l, roomName))
            setReorderingAllowed(true)
            addToBackStack("name")
        }.commit()
    }

    /**
     * Jump to GateWayFragment
     */
    private fun gatewayFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.top_container, GatewayFragment())
            setReorderingAllowed(true)
            addToBackStack("gateway")
        }.commit()
    }

    /**
     * Jump to WisdomFragment
     */
    private fun wisdomFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.top_container, WisdomFragment())
            setReorderingAllowed(true)
            addToBackStack("wisdom")
        }.commit()
    }

    /**
     * Jump to PersonFragment
     */
    private fun personFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.top_container, PersonFragment())
            setReorderingAllowed(true)
            addToBackStack("person")
        }.commit()
    }

    /**
     * Jump to WatchFragment
     */
    private fun watchFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.top_container, WatchFragment())
            setReorderingAllowed(true)
            addToBackStack("watch")
        }.commit()
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