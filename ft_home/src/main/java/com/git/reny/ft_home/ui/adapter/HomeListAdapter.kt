package com.git.reny.ft_home.ui.adapter

import android.widget.ImageView
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.git.reny.ft_home.R
import com.git.reny.ft_home.model.Girl
import com.git.reny.lib_base.base.recycler.BaseDelegateAdapter
import com.git.reny.lib_base.base.recycler.BaseViewHolder
import com.git.reny.lib_base.base.utils.display

class HomeListAdapter: BaseDelegateAdapter<Girl>(
    GridLayoutHelper(2),
    R.layout.item_home_list
) {
    override fun convert(holder: BaseViewHolder, item: Girl?, position: Int) {
        //BaseQuickAdapter
        display(holder.getView<ImageView>(R.id.iv_img), item?.picUrl)
    }

}
