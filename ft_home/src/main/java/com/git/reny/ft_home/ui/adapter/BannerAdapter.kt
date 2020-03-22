package com.git.reny.ft_home.ui.adapter

import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.git.reny.ft_home.R
import com.git.reny.ft_home.model.Girl
import com.git.reny.lib_base.base.extras.showToast
import com.git.reny.lib_base.base.recycler.BaseDelegateAdapter
import com.git.reny.lib_base.base.recycler.BaseViewHolder
import com.git.reny.lib_base.base.widget.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer

class BannerAdapter : BaseDelegateAdapter<List<Girl>>(
    LinearLayoutHelper(),
    R.layout.item_home_banner,
    0,
    1
) {
    override fun convert(holder: BaseViewHolder, item: List<Girl>, position: Int) {
        val mBanner = holder.getView<Banner>(R.id.banner)
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        val imgs = mutableListOf<String>()
        item?.forEach {
            imgs.add(it.picUrl)
        }
        mBanner.setImages(imgs)
        mBanner.setImageLoader(GlideImageLoader())
        mBanner.setBannerAnimation(Transformer.DepthPage)
        mBanner.isAutoPlay(true)
        mBanner.setDelayTime(3000)
        mBanner.setIndicatorGravity(BannerConfig.CENTER)
        mBanner.start()

        mBanner.setOnBannerListener {
            showToast("点击了第 $it 个")
        }
    }

}