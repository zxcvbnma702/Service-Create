package com.example.servicecreate.ui.home.adapter

import android.util.Log
import com.bumptech.glide.Glide
import com.example.base.activity.BaseAdapter
import com.example.servicecreate.R
import com.example.servicecreate.databinding.ItemDialogCustomShowBinding
import com.example.servicecreate.logic.network.model.TaskData
import com.example.servicecreate.ui.home.wisdom.WisdomFragment
import com.kongzue.dialogx.dialogs.MessageDialog

/**
 * @author:SunShibo
 * @date:2023-04-13 19:18
 * @feature:
 */
class ShowCustomTaskRecyclerAdapter(private val fragment: WisdomFragment) : BaseAdapter<TaskData, ItemDialogCustomShowBinding>() {

    private val context = fragment.requireContext()

    override fun ItemDialogCustomShowBinding.onBindViewHolder(bean: TaskData, position: Int) {
        itemCardCustomShowDescription.text = "任务执行时间: " + bean.deleteTime
        itemCardCustomShowName.text = bean.name
        itemCardCustomShowSwitch.isChecked = true
        if(bean.name == "离家模式"){
            Glide.with(context).load(R.drawable.ic_schedule_leave).into(itemCardCustomShowImage)
        }else if (bean.name == "起床模式"){
            Glide.with(context).load(R.drawable.ic_schedule_wake_up).into(itemCardCustomShowImage)
        }
        itemCardCustomShow.setOnLongClickListener{
            MessageDialog.show(context.getString(R.string.wisdom_custom_delete_title),
                context.getString(R.string.wisdom_custom_content) +" " + bean.name, "确定", "取消")
                .setMaskColor(context.getColor(com.kongzue.dialogx.R.color.black30))
                .setCancelable(false)
                .setOkButton { _, _ ->
                    fragment.mViewModel.deleteSchedule(bean.name)
                    false
                }
                .setCancelButton{_, _ ->
                    false
                }
            false
        }
    }
}