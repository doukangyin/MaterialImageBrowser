# Description

仿微信图片浏览界面，主要实现点击小图片放大到详细图浏览的activity跳转动画效果，滚动浏览到别的详细图后返回，详细图也能返回对应的小图。详细图可以缩放、点击返回。

使用时只需要设置transitionName，选中图片的index，传入所有图片的View容器和所有图片的地址，所以支持RecyclerView的各种模式，甚至自定义的多个View

# Captures
![][image-1]


# Example
                ImagePreviewBuilder builder = new ImagePreviewBuilder(mContext);
                builder.setViews()//所有图片容器
                        .setUrl()//所有图片地址
                        .setTransitionName()//transitionName
                        .setCurrentPosition()//所有图片中选中那张图片
                        .setPageVisible()//是否显示下方计数
                        .build();
# About me
- Mail：[doukangyin@gmail.com][1]
- QQ：270271124



[1]:	mailto:doukangyin@gmail.com

[image-1]:	https://raw.githubusercontent.com/HydrDdEtiNy/MaterialImageBrowser/master/captures/demo.gif
