# Multiselect_PhotoView
图片多选和图片预览手势放大缩小的效果
这个版本加了一个activity切换时淡入淡出的动画\<br>
```Java
/** 为activity的切换添加动画 */
int version = Integer.valueOf(android.os.Build.VERSION.SDK);
if(version > 5){
   overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
}
```\<br>
![image](https://github.com/GongXiaoYun/Multiselect_PhotoView/blob/master/screenshot/Screenshot_2016-11-02-14-14-43-890_com.gxy.weixin.png)
![image](https://github.com/GongXiaoYun/Multiselect_PhotoView/blob/master/screenshot/Screenshot_2016-11-02-14-47-14-476_com.gxy.weixin.png)
![image](https://github.com/GongXiaoYun/Multiselect_PhotoView/blob/master/screenshot/Screenshot_2016-11-02-14-47-32-273_com.gxy.weixin.png)
![image](https://github.com/GongXiaoYun/Multiselect_PhotoView/blob/master/screenshot/Screenshot_2016-11-02-14-47-40-650_com.gxy.weixin.png)
