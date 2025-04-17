package com.svga.glide

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import com.bumptech.glide.request.SingleRequest
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.opensource.svgaplayer.SVGADynamicEntity
import com.svga.glide.SVGAGlideEx.log

/**
 * Time:2022/11/26 16:07
 * Author: zhouzechao
 * Description:
 */
private val regexModel by lazy { "model=([a-zA-Z0-9://._-]*)?".toRegex() }

open class SVGAImageViewDrawableTarget(
    imageView: ImageView, var times: Int = 0,
    var repeatMode: Int = ValueAnimator.RESTART,
    val dynamicItem: SVGADynamicEntity = SVGADynamicEntity(),
    var svgaCallback: SVGACallback2? = null,
    var showLastFrame: Boolean = false
) : CustomViewTarget<ImageView, SVGAResource>(imageView) {

    private val TAG = "SVGAImageViewDrawableTarget"

    init {
        request?.clear()
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        clearDrawable("onLoadFailed")
        svgaCallback?.onFailure()
    }

    override fun onResourceReady(
        resource: SVGAResource,
        transition: Transition<in SVGAResource>?
    ) {
        if (resource.model == "") {
            resource.model = " ${(request as? SingleRequest<*>)?.toString()}"
        }
        resource.videoItem ?: kotlin.run {
            svgaCallback?.onFailure()
            return
        }
        if (svgaCallback?.onResourceReady(resource.videoItem, dynamicItem) == false) {
            return
        }
        val drawable = SVGAAnimationDrawable(
            resource.videoItem, times - 1, repeatMode, dynamicItem, showLastFrame
        )
        drawable.tag = resource.model
        drawable.svgaCallback = svgaCallback
        drawable.scaleType = view.scaleType
        resize(resource)
        view.setImageDrawable(drawable)
        prepare(resource, drawable)
        log.d(TAG, "onResourceReady")
    }

    private fun resize(resource: SVGAResource) {
        resource.videoItem ?: return
        val lp = view.layoutParams ?: return
        when {
            lp.width != LayoutParams.WRAP_CONTENT && lp.height == LayoutParams.WRAP_CONTENT -> {
                val ratio = resource.videoItem.videoSize.height / resource.videoItem.videoSize.width
                val height = (lp.width / ratio).toInt()
                if (lp.height != height) {
                    lp.height = height
                    view.layoutParams = lp
                }
            }

            lp.width == LayoutParams.WRAP_CONTENT && lp.height != LayoutParams.WRAP_CONTENT -> {
                val ratio = resource.videoItem.videoSize.height / resource.videoItem.videoSize.width
                val width = (lp.height / ratio).toInt()
                if (lp.width != width) {
                    lp.width = width
                    view.layoutParams = lp
                }
            }

            lp.width == LayoutParams.WRAP_CONTENT && lp.height == LayoutParams.WRAP_CONTENT -> {
                lp.width = resource.videoItem.videoSize.width.toInt()
                lp.height = resource.videoItem.videoSize.height.toInt()
                view.layoutParams = lp
            }
        }
    }

    private fun prepare(resource: SVGAResource, drawable: SVGAAnimationDrawable) {
        try {
            resource.videoItem ?: return
            resource.videoItem.prepare({
                drawable.start(!view.getLoadState())
            }, null)
        } catch (_: Throwable) {
        }
    }

    override fun onResourceCleared(placeholder: Drawable?) {
        clearDrawable("onResourceCleared")
    }

    private fun clearDrawable(reason: String) {
        if (reason.isNotEmpty()) {
            log.d(TAG, "clearDrawable $reason")
        }
        (view.drawable as? SVGAAnimationDrawable)?.stop()
        view.setImageDrawable(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDrawable("onDestroy")
    }
}