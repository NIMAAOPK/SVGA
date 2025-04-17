package com.opensource.svgaplayer

/**
 * Created by cuiminghui on 2017/3/30.
 */
interface SVGACallback {

    fun onSvgaPause()
    fun onFinished()
    fun onRepeat()
    fun onStep(frame: Int, percentage: Double)

}