package com.svga.glide

import android.graphics.Bitmap
import com.opensource.svgaplayer.SVGAVideoEntity


/**
 * Time:2022/11/26 13:59
 * Author: zhouzechao
 * Description:
 */
data class SVGAResource(
    val videoItem: SVGAVideoEntity?,
    var model: String,
    val width: Int,
    val height: Int
) {
    val imageMap: HashMap<String, Bitmap>? by lazy {
        videoItem?.imageMap
    }
}