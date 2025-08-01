package com.en.svga

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.opensource.svgaplayer.SVGAParser

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //SVGACache.onCreate(this.applicationContext)
        SVGAParser.shareParser().init(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_fl, GlideSvgaListFragment(), "ListSvga")
            .commitAllowingStateLoss()
        findViewById<AppCompatButton>(R.id.vp2_btn).setOnClickListener {
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        }
    }
}
