package com.example.servicecreate.ui.home.wisdom

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.base.activity.BaseFragment
import com.example.base.kxt.toast
import com.example.servicecreate.R
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.databinding.FragmentWisdomBinding
import com.example.servicecreate.logic.db.model.CustomDeviceData
import com.example.servicecreate.logic.network.model.*
import com.example.servicecreate.ui.*
import com.example.servicecreate.ui.dialogCancelInfo
import com.example.servicecreate.ui.dialogMessageInfo
import com.example.servicecreate.ui.dialogOkInfo
import com.example.servicecreate.ui.dialogTitleInfo
import com.example.servicecreate.ui.home.adapter.AddCustomTaskRecyclerAdapter
import com.example.servicecreate.ui.home.adapter.ShowCustomTaskRecyclerAdapter
import com.kongzue.dialogx.dialogs.BottomDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.dialogs.PopMenu
import com.kongzue.dialogx.interfaces.OnBindView
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class WisdomFragment : BaseFragment<FragmentWisdomBinding>(), WisdomListener {

    private lateinit var bottomDialog: BottomDialog
    private lateinit var addCustomTaskRecyclerAdapter: AddCustomTaskRecyclerAdapter
    private lateinit var showCustomTaskRecyclerAdapter: ShowCustomTaskRecyclerAdapter

    internal val mViewModel: WisdomViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        )[WisdomViewModel::class.java]
    }

    override fun FragmentWisdomBinding.initBindingView() {
        binding.viewModel = mViewModel
        mViewModel.wisdomListener = this@WisdomFragment

        addCustomTaskRecyclerAdapter = AddCustomTaskRecyclerAdapter(this@WisdomFragment)
        addCustomTaskRecyclerAdapter.setData(mViewModel.selectedDeviceList)

        showCustomTaskRecyclerAdapter = ShowCustomTaskRecyclerAdapter(this@WisdomFragment)
        wisdomRandomList.adapter = showCustomTaskRecyclerAdapter

        wisdomToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        initData()

        wisdomIndoor.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_indoor_title), getString(R.string.wisdom_indoor_content), "确定", "取消")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    mViewModel.controllerIndoor()
                    false
                }
                .setCancelButton{_,_->
                    false
                }
        }

        wisdomOutdoor.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_outdoor_title), getString(R.string.wisdom_outdoor_content), "确定", "取消")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    mViewModel.controllerOutdoor()
                    false
                }
                .setCancelButton{_,_->
                    false
                }
        }

        wisdomSleep.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_sleep_title), getString(R.string.wisdom_sleep_content), "确定", "取消")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    mViewModel.controllerSleep()
                    false
                }
                .setCancelButton{_,_->
                    false
                }
        }

        wisdomWisdom.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_wisdom_title), getString(R.string.wisdom_wisdom_content), "确定", "取消")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    false
                }
                .setCancelButton{_,_->
                    false
                }
        }

        wisdomWisdom2.setOnClickListener {
            MessageDialog.show(getString(R.string.wisdom_wisdom2_title), getString(R.string.wisdom_wisdom2_content), "确定", "取消")
                .setMaskColor(requireContext().getColor(com.kongzue.dialogx.R.color.black30))
                .setTitleTextInfo(dialogTitleInfo(requireContext()))
                .setOkTextInfo(dialogOkInfo(requireContext()))
                .setMessageTextInfo(dialogMessageInfo(requireContext()))
                .setOkButton { _, _ ->
                    false
                }
                .setCancelButton{_,_->
                    false
                }
        }

        wisdomRandom.setOnClickListener {
            if(mViewModel.random){
                wisdomRandomList.visibility = View.VISIBLE
                mViewModel.random = false
                Glide.with(requireContext()).load(R.drawable.ic_outline_cancel_presentation_24).into(wisdomRandomNext)
            }else{
                Glide.with(requireContext()).load(R.drawable.ic_baseline_navigate_next_24).into(wisdomRandomNext)
                mViewModel.random = true
                wisdomRandomList.visibility = View.GONE
            }
        }

        wisdomRandomAdd.setOnClickListener {
            bottomDialog = BottomDialog.show(getString(R.string.wisdom_custom_add_title),
                object : OnBindView<BottomDialog?>(R.layout.layout_dialog_custom) {
                    override fun onBind(dialog: BottomDialog?, v: View) {
                        initDialogView(v)
                    }
                })
                .setRadius(30f)
                .setCancelTextInfo(dialogCancelInfo(requireContext()))
                .setOkButton { dialog, v ->
                false
                }.setCancelButton { dialog, v ->
                false
            }
        }

        wisdomWisdom2Next.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                "开启成功".toast()
                ServiceCreateApplication.sp.edit().putBoolean("wisdom2", true).apply()
            }else{
                "关闭成功".toast()
                ServiceCreateApplication.sp.edit().putBoolean("wisdom2", false).apply()
            }
        }

        wisdomWisdomNext.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                "开启成功".toast()
                ServiceCreateApplication.sp.edit().putBoolean("wisdom1", true).apply()
            }else{
                "关闭成功".toast()
                ServiceCreateApplication.sp.edit().putBoolean("wisdom1", false).apply()
            }
        }

        with(mViewModel){
            lifecycleScope.launch{
                _refresh.collect(){
                    refreshPage()
                }
            }
        }
    }

    private fun initData() {
        mViewModel.getDeviceList()
        mViewModel.getScheduleList()
        binding.wisdomWisdomNext.isChecked = ServiceCreateApplication.sp.getBoolean("wisdom1", false)
        binding.wisdomWisdom2Next.isChecked = ServiceCreateApplication.sp.getBoolean("wisdom2", false)

    }

    private fun initDialogView(v: View) {
        val name = v.findViewById<EditText>(R.id.dialog_custom_task_name)
        val time = v.findViewById<TextView>(R.id.dialog_custom_task_time)
        val list = v.findViewById<RecyclerView>(R.id.dialog_custom_task_list)
        val confirm = v.findViewById<AppCompatButton>(R.id.dialog_custom_ok)
        val cancel = v.findViewById<AppCompatButton>(R.id.dialog_custom_cancel)
        val once = v.findViewById<RadioButton>(R.id.wisdom_custom_add_task_toggle_once)
        val everyday = v.findViewById<RadioButton>(R.id.wisdom_custom_add_task_toggle_everyday)

        name.doAfterTextChanged {
            mViewModel.taskName = it.toString()
        }

        confirm.setOnClickListener {
            mViewModel.checkValid()
        }

        once.setOnClickListener {
            mViewModel.onceOrEvery = 0
        }

        everyday.setOnClickListener {
            mViewModel.onceOrEvery = 1
        }

        cancel.setOnClickListener {
            bottomDialog.dismiss()
        }

        list.adapter = addCustomTaskRecyclerAdapter

        time.setOnClickListener {
            CardDatePickerDialog.builder(requireContext())
                .setTitle(getString(R.string.wisdom_custom_add_task_time))
                .setBackGroundModel(CardDatePickerDialog.STACK)
                .showBackNow(true)
                .setWrapSelectorWheel(false)
                .setThemeColor(R.color.main_color)
                .showDateLabel(true)
                .showFocusDateInfo(true)
                .setLabelText("年","月","日","时","分")
                .setOnChoose("选择"){millisecond->
                    time.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(millisecond );
                    mViewModel.taskTime = time.text.toString()
                }
                .setOnCancel("关闭") {

                }
                .build().show()
        }
    }

    private fun refreshPage(){
        addCustomTaskRecyclerAdapter.setData(mViewModel.selectedDeviceList)
        addCustomTaskRecyclerAdapter.notifyDataSetChanged()
        showCustomTaskRecyclerAdapter.notifyDataSetChanged()
    }

    internal fun showAllDeviceDialog(){
        PopMenu.
        show(mViewModel.allDeviceMap.keys.toTypedArray())
            .setRadius(15f)
            .setMenuTextInfo(dialogTitleInfo(requireContext()))
            .setOnMenuItemClickListener { dialog, text, index ->
                val deviceId = mViewModel.allDeviceMap.values.toList()[index]
                val deviceName = mViewModel.allDeviceMap.keys.toList()[index]
                mViewModel.selectedDeviceList.add(CustomDeviceData(deviceName, deviceId))
                mViewModel.allDeviceMap.remove(deviceName)
                mViewModel.refreshHomePage()
                false
            }
    }

    override fun onIndoorController(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("Indoor"+ response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onOutdoorController(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("OutDoor" +response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onSleepController(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("Sleep" + response.msg + response.data)
                }else{
                    requireContext().toast(response.msg)
                }
            }
        }
    }

    override fun onGetDeviceList(result: LiveData<Result<DeviceListResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    mViewModel.allDeviceMap.putAll(response.data.associate {
                        it.name to it.id.toString()
                    })
                }
                }else{
                    requireContext().toast("数据异常")
                }
            }
        }

    override fun onScheduleOpen(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    requireContext().toast("保存成功")
                    mViewModel.getScheduleList()
                    bottomDialog.dismiss()
                }
            }else{
                requireContext().toast("数据异常")
            }
        }
    }

    override fun onGetScheduleList(result: LiveData<Result<ScheduleTaskResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    showCustomTaskRecyclerAdapter.setData(response.data)
                    showCustomTaskRecyclerAdapter.notifyDataSetChanged()
                }
            }else{
                requireContext().toast("数据异常")
            }
        }
    }

    override fun onDeleteSchedule(result: LiveData<Result<SendVerifiedResponse>>) {
        result.observe(this){ re ->
            val response = re.getOrNull()
            if (response != null) {
                if(response.code == 1){
                    "删除成功".toast()
                    mViewModel.getScheduleList()
                    mViewModel.refreshHomePage()
                }
            }else{
                requireContext().toast("数据异常")
            }
        }
    }


}