package com.example.servicecreate.ui.home.adapter

import android.view.View
import com.bumptech.glide.Glide
import com.example.base.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemDialogCustomBinding
import com.example.servicecreate.logic.db.model.CustomDeviceData
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.logic.network.model.Item
import com.example.servicecreate.ui.home.wisdom.WisdomFragment

/**
 * @author:SunShibo
 * @date:2023-04-12 21:11
 * @feature:
 */
class AddCustomTaskRecyclerAdapter(private val fragment: WisdomFragment): BaseAdapter<CustomDeviceData, ItemDialogCustomBinding>() {

    var isCheck: Boolean = false

    override fun ItemDialogCustomBinding.onBindViewHolder(bean: CustomDeviceData, position: Int) {
        if(bean.deviceId == "0"){
            itemDeviceName.text = "点击选择设备"
            itemDeviceSwitch.visibility = View.GONE
            itemBlue.setOnClickListener {
                fragment.showAllDeviceDialog()
            }
        }else{
            itemDeviceName.text = bean.deviceName
            itemDeviceSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                fragment.mViewModel.sendNetMap[bean] = isChecked
                if(isChecked){
                    isCheck = isChecked
                    fragment.mViewModel.sendNetList.add(Item(bean.deviceId, "1"))
                }else{
                    isCheck = isChecked
                    fragment.mViewModel.sendNetList.add(Item(bean.deviceId, "0"))
                }
            }
            Glide.with(fragment.requireContext()).load(R.drawable.ic_outline_cancel_24).into(itemDeviceImageView)
            itemDeviceImageView.setOnClickListener {
                if(isCheck){
                    fragment.mViewModel.sendNetList.remove(Item(bean.deviceId, "1"))
                    fragment.mViewModel.selectedDeviceList.remove(bean)
                    fragment.mViewModel.allDeviceMap[bean.deviceName] = bean.deviceId
                }else{
                    fragment.mViewModel.sendNetList.remove(Item(bean.deviceId, "0"))
                    fragment.mViewModel.selectedDeviceList.remove(bean)
                    fragment.mViewModel.allDeviceMap[bean.deviceName] = bean.deviceId
                }
                fragment.mViewModel.refreshHomePage()
            }
        }

    }
}