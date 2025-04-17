package com.en.svga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.svga.glide.clearSvga
import com.svga.glide.loadSvga
import com.svga.glide.pauseSvga
import com.svga.glide.resumeSvga

class GlideSvgaListFragment : Fragment() {

    private var isLoad = false
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rv = RecyclerView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        return rv
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    updateListSvgaState(false)
                }

                Lifecycle.Event.ON_RESUME -> {
                    if (!isLoad) {
                        isLoad = true
                        rv.adapter = RvAdapter(genFakeData())
                    } else {
                        updateListSvgaState(true)
                    }
                }

                else -> {
                }
            }
        })
    }

    /**
     * ViewPager2场景
     */
    private fun updateListSvgaState(resume: Boolean) {
        (rv.adapter as? RvAdapter)?.let {
            val layoutManager = rv.layoutManager as LinearLayoutManager
            val startPosition = layoutManager.findFirstVisibleItemPosition()
            val endPosition = layoutManager.findLastVisibleItemPosition()
            if (startPosition == -1 || endPosition == -1) {
                return
            }
            for (i in (startPosition..endPosition)) {
                val itemView = layoutManager.findViewByPosition(i)
                it.updateSvgaState(itemView, resume)
            }
        }
    }

    private fun genFakeData(): List<String> {
        val bgUrl =
            "file:///android_asset/gradientBorder.svga"
        val bgUrl1 =
            "https://oss.qingyujiaoyou.com/boss/pc__config_vrn66ladnywcbd34n09gkxgv0puh3ads.svga"
        val bgUrl2 =
            "https://oss.qingyujiaoyou.com/boss/pc__config_wijwmlwu1nlxv1bel211osncczj300de.svga"
        val bgUrl3 =
            "https://oss.qingyujiaoyou.com/boss/pc__config_5ca5r7vhkx41pbtuk4sffrg8ivi5r9cx.svga"
        val list = mutableListOf<String>()
        for (i in 0 until 100) {
            val url = when (i % 5) {
                0 -> bgUrl
                1 -> bgUrl1
                2 -> bgUrl2
                3 -> bgUrl3
                else -> ""
            }
            list.add(url)
        }
        return list
    }

    private class RvAdapter(val data: List<String>) : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false))
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            holder.bind(data[position])
        }

        fun updateSvgaState(itemView: View?, resume: Boolean) {
            itemView ?: return
            val iv = itemView.findViewById<ImageView>(R.id.iv)
            if (resume) {
                iv.resumeSvga(true)
            } else {
                iv.pauseSvga(true)
            }
        }
    }

    private class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iv = itemView.findViewById<ImageView>(R.id.iv)

        fun bind(url: String) {
            if (url.isNotEmpty()) {
                iv.loadSvga(url)
            } else {
                iv.clearSvga()
            }
        }
    }
}