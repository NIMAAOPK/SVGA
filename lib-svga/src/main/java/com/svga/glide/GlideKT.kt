package com.svga.glide

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.opensource.svgaplayer.SVGADynamicEntity
import com.svga.glide.SVGAGlideEx.log

/**
 * Time:2023/3/17 12:02
 * Author: zhouzechao
 * Description:
 */
@SuppressLint("ClickableViewAccessibility")
fun SVGAImageViewDrawableTarget.setSvgaClickMapListener(
    clickKey: List<String>,
    listener: (String) -> Unit
) {
    view.isClickable = true
    dynamicItem.setClickArea(clickKey)
    val map = dynamicItem.mClickMap
    view.setOnTouchListener { v, event ->
        if (event?.action != MotionEvent.ACTION_DOWN) {
            return@setOnTouchListener false
        }
        log.d(
            "GlideKT",
            "setOnTouchListener event?.action:${event.action} x:${event.x} y:${event.y}"
        )
        for ((key, value) in map) {
            log.d("GlideKT", "for key:${key} value:${value[0]}-${value[1]}-${value[2]}-${value[3]}")
            if (event.x >= value[0] && event.x <= value[2] && event.y >= value[1] && event.y <= value[3]) {
                listener.invoke(key)
                return@setOnTouchListener true
            }
        }
        return@setOnTouchListener false
    }
}

fun ImageView?.loadSvga(source: String?, skipMemoryCache: Boolean = false) {
    this ?: return
    loadSvga(source, skipMemoryCache, 0, false)
}

fun ImageView?.loadSvga(
    source: String?,
    skipMemoryCache: Boolean = false,
    times: Int = 0,
    showLastFrame: Boolean = false,
    repeatMode: Int = ValueAnimator.RESTART,
    dynamicItem: SVGADynamicEntity = SVGADynamicEntity(),
    svgaCallback: SVGACallback2? = null
) {
    if (this == null || source.isNullOrEmpty()) {
        return
    }
    if (context is FragmentActivity && (context as Activity).isDestroyed) {
        return
    }
    val override =
        layoutParams == null || layoutParams.width == LayoutParams.WRAP_CONTENT || layoutParams.height == LayoutParams.WRAP_CONTENT
    var rq = SVGAExtensions.asSVGAResource(Glide.with(this).`as`(SVGAResource::class.java))
    if (override) {
        rq = rq.override(Target.SIZE_ORIGINAL)
    }
    val target = this.svgaGlideTarget(dynamicItem = dynamicItem)!!.also {
        it.times = times
        it.showLastFrame = showLastFrame
        it.repeatMode = repeatMode
        it.svgaCallback = svgaCallback
    }
    updateLoadState(false)
    rq.load(source).skipMemoryCache(skipMemoryCache).into(target)
}

fun ImageView?.updateLoadState(paused: Boolean) {
    this ?: return
    setTag(R.id.svga_glide_state, paused)
}

/** return false when load state is loading **/
fun ImageView?.getLoadState(): Boolean {
    this ?: return false
    return (getTag(R.id.svga_glide_state) as? Boolean) ?: false
}

fun ImageView?.pauseSvga(isInitiative: Boolean = false) {
    this ?: return
    updateLoadState(true)
    if (drawable is SVGAAnimationDrawable) {
        (drawable as SVGAAnimationDrawable).pause(isInitiative)
        return
    }
}

fun ImageView?.resumeSvga(isInitiative: Boolean = false) {
    this ?: return
    updateLoadState(false)
    if (drawable is SVGAAnimationDrawable) {
        (drawable as SVGAAnimationDrawable).resume(isInitiative)
        return
    }
}

fun ImageView?.isSvgaAnimating(): Boolean {
    this ?: return false
    return (this.drawable as? SVGAAnimationDrawable)?.isRunning ?: false
}

fun ImageView?.clearSvga() {
    this ?: return
    val target = this.svgaGlideTarget(false)
    if (target != null) {
        Glide.with(this).clear(target)
    }
    (this.drawable as? SVGAAnimationDrawable)?.stop()
    this.setImageDrawable(null)
}

fun ImageView?.svgaGlideTarget(
    autoCreate: Boolean = true,
    dynamicItem: SVGADynamicEntity = SVGADynamicEntity()
): SVGAImageViewDrawableTarget? {
    this ?: return null
    var target = this.getTag(R.id.svga_glide_target)
    if (target == null && autoCreate) {
        target = SVGAImageViewDrawableTarget(this, dynamicItem = dynamicItem)
        this.setTag(R.id.svga_glide_target, target)
    }
    return target as? SVGAImageViewDrawableTarget
}

