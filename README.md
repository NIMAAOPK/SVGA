# 通过Glide加载SVGA文件
# 原项目[SVGA_Glide_RecyclerView](https://github.com/zzechao/SVGA_Glide_RecyclerView/tree/master)
合并原项目代码，删除compiler部分代码

新增了部分kotlin扩展方法

主要对原项目进行了以下修改：

1:修复内存泄漏问题；

2:修复在RecyclerView场景下item回收导致动画播放混乱；

3:优化了部分特殊场景下动画播放的时机，避免了不必要的动画播放；

4:新增对Viewpager+fragment场景生命周期管理demo；

