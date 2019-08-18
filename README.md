# PraiseView

#### 使用极少量代码实现一个带数字显示的star控件，具有圆环水波纹效果和数字位移效果

项目特点
* 点击star图标有圆环波纹效果
* 数字自增支持位移效果（上进下出），数字自减支持位移效果（下进上出）

## APK下载
[Download](https://github.com/YangJ0720/PraiseView/blob/master/apk/app-debug.apk)

## XML配置
```xml
<?xml version="1.0" encoding="utf-8"?>
<merge xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       android:layout_width="match_parent"
       android:layout_height="wrap_content">

    <com.example.praise.PraiseIconView
            android:id="@+id/praiseIconView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            app:circleColor="@color/colorCircle"
            app:bitmapNormal="@drawable/ic_detail_replay_collect_normal"
            app:bitmapPressed="@drawable/ic_detail_replay_collect_pressed"/>

    <com.example.praise.PraiseTextView
            android:id="@+id/praiseTextView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            app:textSize="16sp"
            app:textColor="@android:color/black"/>

</merge>
```
## 属性说明
|属性|说明|
|----|-----
|circleColor|点击光波效果颜色
|bitmapNormal|未选中显示的图片
|bitmapPressed|选中显示的图片
|textSize|字体大小
|textColor|字体颜色

## 效果图
如下图所示：

![image](https://github.com/YangJ0720/PraiseView/blob/master/gif/preview.gif)
