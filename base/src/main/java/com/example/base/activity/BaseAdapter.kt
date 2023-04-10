package com.example.base.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.base.R
import com.example.base.kxt.getViewBinding
import java.util.*

/**
 * @author:SunShibo
 * @date:2022-09-27 22:10
 * @feature  Unified Adapter initialization work.
Simplify the use of onBindViewHolder.
Remove the need to repeatedly create ViewHolder every time.
Unify the way we set the listener event for Item.
Unified Adapter data refresh.
 */
abstract class BaseAdapter<T, VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseAdapter.BindViewHolder<VB>>() {

    private var mData: List<T> = mutableListOf()

    /**
     * Use DiffUtil class to compare data and refresh View
     */
    fun setData(data: List<T>?) {
        data?.let {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mData.size
                }

                override fun getNewListSize(): Int {
                    return it.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldData: T = mData[oldItemPosition]
                    val newData: T = it[newItemPosition]
                    return this@BaseAdapter.areItemsTheSame(oldData, newData)
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldData: T = mData[oldItemPosition]
                    val newData: T = it[newItemPosition]
                    return this@BaseAdapter.areItemContentsTheSame(
                        oldData,
                        newData,
                        oldItemPosition,
                        newItemPosition
                    )
                }
            })
            mData = data
            result.dispatchUpdatesTo(this)
        } ?: let {
            mData = mutableListOf()
            notifyItemRangeChanged(0, mData.size)
        }

    }

    fun addData(data: List<T>?, position: Int? = null) {
        if (data.isNullOrEmpty()) return
        with(LinkedList(mData)) {
            position?.let {
                val startPosition = when {
                    it < 0 -> 0
                    it >= size -> size
                    else -> it
                }
                addAll(startPosition, data)
            } ?: addAll(data)
            setData(this)
        }
    }

    protected open fun areItemContentsTheSame(
        oldItem: T,
        newItem: T,
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean =
        oldItem == newItem

    protected open fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        oldItem == newItem

    fun getData(): List<T> = mData

    fun getItem(position: Int): T = mData[position]

    fun getActualPosition(data: T): Int = mData.indexOf(data)

    override fun getItemCount(): Int = mData.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder<VB> {
        return with(getViewBinding<VB>(LayoutInflater.from(parent.context), parent, 1)) {
            setListener()
            BindViewHolder(this)
        }
    }

    override fun onViewAttachedToWindow(holder: BindViewHolder<VB>) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.apply {
            clearAnimation()
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_in_scroll))
        }
    }

    override fun onBindViewHolder(holder: BindViewHolder<VB>, position: Int) {
        with(holder.binding) {
            holder.layoutPosition
            onBindViewHolder(getItem(position), position)
            executePendingBindings()
        }
    }

    /**
     * Treat setListener only as an optional item
     */
    open fun VB.setListener() {}

    /**
     * Binding processing is handled by this subclass
     */
    abstract fun VB.onBindViewHolder(bean: T, position: Int)

    /**
     * Create a unified ViewHolder
     */
    class BindViewHolder<M : ViewDataBinding>(var binding: M) :
        RecyclerView.ViewHolder(binding.root)
}