package com.git.reny.lib_base.base.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import java.util.*

abstract class BaseDelegateAdapter<T>(
    private val layoutHelper: LayoutHelper,
    private val layoutId: Int,
    private val mCount: Int? = null,
    data: MutableList<T>? = null
) : DelegateAdapter.Adapter<BaseViewHolder>() {

    var data: MutableList<T> = data ?: arrayListOf()
        internal set

    private var mOnItemClickListener: ((adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Unit)? = null
    private var mOnItemLongClickListener: ((adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Boolean)? = null
    private var mOnItemChildClickListener: ((adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Unit)? = null
    private var mOnItemChildLongClickListener: ((adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Boolean)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (layoutId == 0) {
            throw RuntimeException("${this.javaClass.simpleName}: layoutId == 0")
        }
        return layoutId
    }

    override fun getItemCount(): Int = mCount ?: data.size

    override fun onCreateLayoutHelper(): LayoutHelper = layoutHelper

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, getItem(position), position)
        bindViewClickListener(holder, getItemViewType(position), position)
    }

    open fun getItem(@IntRange(from = 0) position: Int): T? {
        return if(position < data.size) data[position] else null
    }

    protected abstract fun convert(holder: BaseViewHolder, item: T?, position: Int)


    open fun clear(){
        this.data.clear()
        notifyDataSetChanged()
    }

    open fun setNewData(data: MutableList<T>?) {
        if (data == this.data) {
            return
        }
        this.data = data ?: arrayListOf()
        notifyDataSetChanged()
    }

    protected fun compatibilityDataSizeChanged(size: Int) {
        if (this.data.size == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    open fun addData(@IntRange(from = 0) position: Int, data: T) {
        this.data.add(position, data)
        notifyItemInserted(position)
        compatibilityDataSizeChanged(1)
    }

    /**
     * add one new data
     * 添加一条新数据
     */
    open fun addData(@NonNull data: T) {
        this.data.add(data)
        notifyItemInserted(this.data.size)
        compatibilityDataSizeChanged(1)
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    open fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        this.data.addAll(position, newData)
        notifyItemRangeInserted(position, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    open fun addData(@NonNull newData: Collection<T>) {
        this.data.addAll(newData)
        notifyItemRangeInserted(this.data.size - newData.size, newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    open fun remove(@IntRange(from = 0) position: Int) {
        if (position >= data.size) {
            return
        }
        this.data.removeAt(position)
        val internalPosition = position
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, this.data.size - internalPosition)
    }

    open fun remove(data: T) {
        val index = this.data.indexOf(data)
        if (index == -1) {
            return
        }
        remove(index)
    }

    /**
     * change data
     * 改变某一位置数据
     */
    open fun setData(@IntRange(from = 0) index: Int, data: T) {
        if (index >= this.data.size) {
            return
        }
        this.data[index] = data
        notifyItemChanged(index)
    }

    /**
     * use data to replace all item in mData. this method is different [setNewData],
     * it doesn't change the [BaseQuickAdapter.data] reference
     *
     * @param newData data collection
     */
    open fun replaceData(newData: Collection<T>) {
        // 不是同一个引用才清空列表
        if (newData != this.data) {
            this.data.clear()
            this.data.addAll(newData)
        }
        notifyDataSetChanged()
    }



    protected open fun setOnItemClick(v: View, position: Int) {
        mOnItemClickListener?.let { it(this, v, position) }
    }
    protected open fun setOnItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.let { it(this, v, position) } ?: false
    }

    //先调用 addChildClickViewIds
    protected open fun setOnItemChildClick(v: View, position: Int) {
        mOnItemChildClickListener?.let { it(this, v, position) }
    }

    //先调用 addChildLongClickViewIds
    protected open fun setOnItemChildLongClick(v: View, position: Int): Boolean {
        return mOnItemChildLongClickListener?.let { it(this, v, position) } ?: false
    }


    /**
     * 绑定 item 点击事件
     * @param viewHolder VH
     */
    protected open fun bindViewClickListener(viewHolder: BaseViewHolder, viewType: Int, position: Int) {
        mOnItemClickListener?.let {
            viewHolder.itemView.setOnClickListener { v ->
                //val position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                setOnItemClick(v, position)
            }
        }
        mOnItemLongClickListener?.let {
            viewHolder.itemView.setOnLongClickListener { v ->
                //var position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnLongClickListener false
                }
                setOnItemLongClick(v, position)
            }
        }

        mOnItemChildClickListener?.let {
            for (id in getChildClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isClickable) {
                        childView.isClickable = true
                    }
                    childView.setOnClickListener { v ->
                        //var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnClickListener
                        }
                        setOnItemChildClick(v, position)
                    }
                }
            }
        }
        mOnItemChildLongClickListener?.let {
            for (id in getChildLongClickViewIds()) {
                viewHolder.itemView.findViewById<View>(id)?.let { childView ->
                    if (!childView.isLongClickable) {
                        childView.isLongClickable = true
                    }
                    childView.setOnLongClickListener { v ->
                        //var position = viewHolder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnLongClickListener false
                        }
                        setOnItemChildLongClick(v, position)
                    }
                }
            }
        }
    }


    private val childClickViewIds = LinkedHashSet<Int>()
    fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }
    fun addChildClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childClickViewIds.add(viewId)
        }
    }


    private val childLongClickViewIds = LinkedHashSet<Int>()
    fun getChildLongClickViewIds(): LinkedHashSet<Int> {
        return childLongClickViewIds
    }
    fun addChildLongClickViewIds(@IdRes vararg viewIds: Int) {
        for (viewId in viewIds) {
            childLongClickViewIds.add(viewId)
        }
    }

    fun setOnItemClickListener(listener: (adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Unit) {
        this.mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Boolean) {
        this.mOnItemLongClickListener = listener
    }

    fun setOnItemChildClickListener(listener: (adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Unit) {
        this.mOnItemChildClickListener = listener
    }

    fun setOnItemChildLongClickListener(listener: (adapter: BaseDelegateAdapter<*>, view: View, position: Int) -> Boolean) {
        this.mOnItemChildLongClickListener = listener
    }

}