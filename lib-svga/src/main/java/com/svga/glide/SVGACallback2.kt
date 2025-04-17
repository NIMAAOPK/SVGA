package com.svga.glide

import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAVideoEntity


/**
 * @author:zhouz
 * @date: 2024/8/2 18:47
 * description：针对glide 加载
 */
interface SVGACallback2 : SVGACallback {

    fun onSvgaStart() {}

    // 只有Glide方法加载才有回调
    fun onSvgaResume() {}

    // 只有Glide方法加载才有回调
    fun onFailure() {}

    fun onResourceReady(item: SVGAVideoEntity, dynamicItem: SVGADynamicEntity): Boolean = true

    override fun onSvgaPause() {}

    override fun onFinished() {}

    override fun onRepeat() {}

    override fun onStep(frame: Int, percentage: Double) {}
}