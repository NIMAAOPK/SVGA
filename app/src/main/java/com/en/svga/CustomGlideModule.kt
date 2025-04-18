package com.en.svga

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.svga.glide.SVGAGlideEx
import java.io.File

@GlideModule
class CustomGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val cachePath = context.cacheDir.absolutePath + File.separatorChar + "glide-svga"
        SVGAGlideEx.register(glide, registry, cachePath)
    }
}