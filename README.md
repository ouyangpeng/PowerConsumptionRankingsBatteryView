# PowerConsumptionRankingsBatteryView
自定义一个电池View

# 一、背景
最近公司有个业务，需要自定义一个电池控件，因此自己按照UI图编写了一个自定义View。

# 二、效果

![这里写图片描述](https://img-blog.csdn.net/20180615162931331?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![这里写图片描述](https://img-blog.csdn.net/20180615160601248?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![这里写图片描述](https://img-blog.csdn.net/20180615155731624?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![这里写图片描述](https://img-blog.csdn.net/20180615155750874?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


# 三、实现过程

首先看下视觉给出的UI效果图 

![这里写图片描述](https://img-blog.csdn.net/20180615160810101?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

从这里我们可以看得出来，要自定义这个电池View的话，分为3部分来绘制。

 1. 第一部分是电池头
 2. 第二部分是电池边框
 3. 第三部分是电池电量

## 3.1 自定义属性
因此按照上面的UI效果图来做的话，我们自定义几个属性。

attrs.xml

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--自定义耗电排行电池View的自定义属性-->
    <declare-styleable name="PowerConsumptionRankingsBatteryView">
        <!-- 手表低电时候的电池颜色 -->
        <attr name="batteryLowerPowerColor" format="color"/>
        <!-- 手表在线时候的电池颜色 -->
        <attr name="batteryOnlineColor" format="color"/>
        <!-- 手表离线时候的电池颜色 -->
        <attr name="batteryOfflineColor" format="color"/>
        <!--  电池最大高度 -->
        <attr name="batteryLevelMaxHeight" format="integer"/>
        <!--  电池宽度 -->
        <attr name="batteryLevelWidth" format="integer"/>
        <!--  外壳的相关信息 -->
        <attr name="batteryShellCornerRadius" format="dimension"/>
        <attr name="batteryShellWidth" format="dimension"/>
        <attr name="batteryShellHeight" format="dimension"/>
        <attr name="batteryShellStrokeWidth" format="dimension"/>
        <!--  电池头的相关信息 -->
        <attr name="batteryShellHeadCornerRadius" format="dimension"/>
        <attr name="batteryShellHeadWidth" format="dimension"/>
        <attr name="batteryShellHeadHeight" format="dimension"/>
        <!-- 电池外壳和电池等级直接的间距 -->
        <attr name="batteryGap" format="dimension"/>
    </declare-styleable>

</resources>
```
执行自定义属性，包括电池颜色、电池最大高度、电池宽度、电池头部的相关信息和电池边框的相关信息。

colors.xml  定义了一些UI标注的颜色值

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="colorPrimary">#3F51B5</color>
    <color name="colorPrimaryDark">#303F9F</color>
    <color name="colorAccent">#FF4081</color>


    <color name="lowerPowerColor">#ff4d4d</color>
    <color name="onlineColor">#00d68d</color>
    <color name="offlineColor">#bfbfbf</color>
</resources>
```
dimens.xml    定义了一些UI标注的尺寸值

```
<resources>
    <!-- Default screen margins, per the Android Design guidelines. -->
    <!--电池控件 高度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_height">48dp</dimen>
    <!--电池控件 宽度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_width">31dp</dimen>

    <!--电池外壳 厚度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_shell_stroke_width">2.5dp
    </dimen>
    <!--电池外壳 宽度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_shell_width">28dp</dimen>
    <!--电池外壳 高度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_shell_height">42dp</dimen>
    <!--电池外壳 圆角-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_shell_corner">3dp</dimen>

    <!--电池头 宽度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_head_width">14dp</dimen>
    <!--电池头 高度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_head_height">3dp</dimen>
    <!--电池头 圆角-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_head_corner">2dp</dimen>

    <!--电池外壳和电池等级直接的间距-->
    <dimen name="power_consumption_rankings_dimen_main_battery_view_gap">2dp</dimen>

    <!--电池 宽度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_level_width">22dp</dimen>
    <!--电池 最大高度-->
    <dimen name="power_consumption_rankings_dimen_main_battery_level_max_height">35dp</dimen>
</resources>

```

## 3.2 实现自定义View

创建PowerConsumptionRankingsBatteryView继承View，
在View的构造方法中，获取我们需要的自定义样式，重写onMesure，onDraw方法。

```
package com.oyp.battery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * 自定义 耗电排行内的 电池图标
 * </p>
 * created by OuyangPeng at 2018/6/10 下午 05:57
 *
 * @author OuyangPeng
 */
public class PowerConsumptionRankingsBatteryView extends View {
    /**
     * 电池最大电量
     */
    public static final int MAX_LEVEL = 100;
    /**
     * 电池默认电量
     */
    public static final int DEFAULT_LEVEL = 40;

    /**
     * 自定义View的宽
     */
    private int width;
    /**
     * 自定义View的高
     */
    private int height;
    /**
     * 抗锯齿标志
     */
    private DrawFilter drawFilter;
    /**
     * 电池外壳 厚度
     */
    private int shellStrokeWidth;
    /**
     * 电池外壳 圆角
     */
    private int shellCornerRadius;
    /**
     * 电池外壳 宽度
     */
    private int shellWidth;
    /**
     * 电池外壳 宽度
     */
    private int shellHeight;
    /**
     * 电池头 圆角
     */
    private int shellHeadCornerRadius;
    /**
     * 电池头 宽度
     */
    private int shellHeadWidth;
    /**
     * 电池头 高度
     */
    private int shellHeadHeight;

    /**
     * 电池宽度
     */
    private int levelWidth;
    /**
     * 电池最大高度
     */
    private int levelMaxHeight;
    /**
     * 电池高度
     */
    private int levelHeight = 100;

    /**
     * 电池外壳和电池等级直接的间距
     */
    private int gap;

    /**
     * 电池外壳 画笔
     */
    private Paint shellPaint;
    /**
     * 电池外壳
     */
    private RectF shellRectF;
    /**
     * 电池头
     */
    private RectF shellHeadRect;

    /**
     * 电池电量 画笔
     */
    private Paint levelPaint;
    /**
     * 电池电量
     */
    private RectF levelRect;

    /**
     * 低电颜色
     */
    private int lowerPowerColor;
    /**
     * 在线颜色
     */
    private int onlineColor;
    /**
     * 离线颜色
     */
    private int offlineColor;

    public PowerConsumptionRankingsBatteryView(Context context) {
        super(context);
    }

    public PowerConsumptionRankingsBatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        initTypeArray(context, attrs);

        //设置 电池壳画笔的相关属性
        shellPaint = new Paint();
        shellPaint.setAntiAlias(true);
        shellPaint.setColor(onlineColor);
        shellPaint.setStrokeWidth(shellStrokeWidth);
        shellPaint.setAntiAlias(true);

        //设置 电池画笔的相关属性
        levelPaint = new Paint();
        levelPaint.setColor(onlineColor);
        levelPaint.setStyle(Paint.Style.FILL);
        levelPaint.setStrokeWidth(levelWidth);

        shellRectF = new RectF();
        shellHeadRect = new RectF();
        levelRect = new RectF();
    }

    /**
     * 初始化自定义属性
     */
    private void initTypeArray(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PowerConsumptionRankingsBatteryView);
        lowerPowerColor = typedArray.getColor(R.styleable.PowerConsumptionRankingsBatteryView_batteryLowerPowerColor,
                getResources().getColor(R.color.lowerPowerColor));
        onlineColor = typedArray.getColor(R.styleable.PowerConsumptionRankingsBatteryView_batteryOnlineColor,
                getResources().getColor(R.color.onlineColor));
        offlineColor = typedArray.getColor(R.styleable.PowerConsumptionRankingsBatteryView_batteryOfflineColor,
                getResources().getColor(R.color.offlineColor));
        //外壳的相关信息
        shellCornerRadius = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellCornerRadius,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_corner));
        shellWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_width));
        shellHeight = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellHeight,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_height));
        shellStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellStrokeWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_shell_stroke_width));

        //电池头的相关信息
        shellHeadCornerRadius = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellHeadCornerRadius,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_head_corner));
        shellHeadWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellHeadWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_head_width));
        shellHeadHeight = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryShellHeadHeight,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_head_height));

        //电池最大高度
        levelMaxHeight = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryLevelMaxHeight,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_level_max_height));
        //电池宽度
        levelWidth = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryLevelWidth,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_level_width));

        //电池外壳和电池等级直接的间距
        gap = typedArray.getDimensionPixelOffset(R.styleable.PowerConsumptionRankingsBatteryView_batteryGap,
                getResources().getDimensionPixelOffset(R.dimen.power_consumption_rankings_dimen_main_battery_view_gap));
        //回收typedArray
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //对View上的內容进行测量后得到的View內容占据的宽度
        width = getMeasuredWidth();
        //对View上的內容进行测量后得到的View內容占据的高度
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(drawFilter);

        // 电池头 矩形的坐标

        //坐标 left：控件整体宽度的一半 减去 电池头宽度的一半
        shellHeadRect.left = width / 2 - shellHeadWidth / 2;
        //坐标 top： 0
        shellHeadRect.top = 0;
        //坐标 right：控件整体宽度的一半 加上 电池头宽度的一半
        shellHeadRect.right = width / 2 + shellHeadWidth / 2;
        //坐标 bottom：电池头的高度
        shellHeadRect.bottom = shellHeadHeight;

        // 电池壳 矩形的坐标

        //坐标 left：电池壳厚度的一半
        shellRectF.left = shellStrokeWidth / 2;
        //坐标 left：电池壳厚度的一半 加上 电池头的高度
        shellRectF.top = shellStrokeWidth / 2 + shellHeadHeight;
        //坐标 right：控件整体宽度 减去 电池壳厚度的一半
        shellRectF.right = width - shellStrokeWidth / 2;
        //坐标 bottom：控件整体高度 减去 电池壳厚度的一半
        shellRectF.bottom = height - shellStrokeWidth / 2;

        // 电池电量 矩形的坐标

        //坐标 left：电池壳厚度的一半 加上 电池外壳和电池等级直接的间距
        levelRect.left = shellStrokeWidth + gap;

        //电池满格时候的最大高度 ：（控件整体高度  减去电池壳厚度 减去电池头高度 减去 电池外壳和电池等级直接的间距的两倍）
        //topOffset: 电池满格时候的最大高度 * （电池满格100 - 当前的电量）/ 电池满格100
        float topOffset = (height - shellHeadHeight - gap * 2 - shellStrokeWidth) * (MAX_LEVEL - levelHeight) / MAX_LEVEL;
        //坐标 top：电池头的高度 + 电池壳的厚度 + 电池外壳和电池等级直接的间距 + topOffset
        levelRect.top = shellHeadHeight + shellStrokeWidth + gap + topOffset;

        //坐标 right：控件整体宽度 减去 电池壳厚度的一半 减去 电池外壳和电池等级直接的间距
        levelRect.right = width - shellStrokeWidth - gap;

        //坐标 bottom：控件整体宽度 减去 电池壳厚度的一半 减去 电池外壳和电池等级直接的间距
        levelRect.bottom = height - shellStrokeWidth - gap;

        //绘制电池头
        shellPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(shellHeadRect, shellHeadCornerRadius, shellHeadCornerRadius, shellPaint);
        //由于电池头的左下角和右下角不是圆角的，因此我们需要画一个矩形 覆盖圆角
        //绘制左下角矩形
        canvas.drawRect(shellHeadRect.left, shellHeadRect.bottom - shellHeadCornerRadius,
                shellHeadRect.left + shellHeadCornerRadius, shellHeadRect.bottom, shellPaint);
        //绘制右下角矩形
        canvas.drawRect(shellHeadRect.right - shellHeadCornerRadius, shellHeadRect.bottom - shellHeadCornerRadius,
                shellHeadRect.right, shellHeadRect.bottom, shellPaint);

        //绘制电池壳
        shellPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(shellRectF, shellCornerRadius, shellCornerRadius, shellPaint);

        //绘制电池等级
        canvas.drawRect(levelRect, levelPaint);
    }

    /**
     * 设置电池电量
     *
     * @param level
     */
    public void setLevelHeight(int level) {
        this.levelHeight = level;
        if (this.levelHeight < 0) {
            levelHeight = MAX_LEVEL;
        } else if (this.levelHeight > MAX_LEVEL) {
            levelHeight = MAX_LEVEL;
        }
        postInvalidate();
    }

    /**
     * 设置在线 重绘
     */
    public void setOnline() {
        shellPaint.setColor(onlineColor);
        levelPaint.setColor(onlineColor);
        postInvalidate();
    }

    /**
     * 设置离线 重绘
     */
    public void setOffline() {
        shellPaint.setColor(offlineColor);
        levelPaint.setColor(offlineColor);
        postInvalidate();
    }

    /**
     * 设置低电 重绘
     */
    public void setLowerPower() {
        shellPaint.setColor(lowerPowerColor);
        levelPaint.setColor(lowerPowerColor);
        postInvalidate();
    }
}

```

## 3.3 使用我们的自定义View

activity_main.xml 中使用我们的自定义View

```
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/constraintLayout"
    tools:context=".MainActivity">

    <com.oyp.battery.PowerConsumptionRankingsBatteryView
        android:id="@+id/mPowerConsumptionRankingsBatteryView"
        android:layout_width="@dimen/power_consumption_rankings_dimen_main_battery_view_width"
        android:layout_height="@dimen/power_consumption_rankings_dimen_main_battery_view_height"
        android:layout_marginTop="30dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/tv1"
        android:layout_width="match_parent"
        android:layout_height="86dp"
        android:text="@string/ouyangpeng"
        android:gravity="center_horizontal"
        app:layout_constraintEnd_toEndOf="@id/constraintLayout"
        app:layout_constraintStart_toStartOf="@id/constraintLayout"
        app:layout_constraintTop_toTopOf="@+id/constraintLayout"
        app:layout_constraintBottom_toBottomOf="@+id/constraintLayout"
        />

</android.support.constraint.ConstraintLayout>
```

## 3.4 动态展示不同状态下的电池View

MainActivity.java 文件中 动态更新电池的状态

```
package com.oyp.battery;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
/**
 *  自定义View 展示界面
 * </p>
 * created by OuyangPeng at 2018/6/15 下午 03:33
 * @author OuyangPeng
 */
public class MainActivity extends AppCompatActivity implements BaseHandlerCallBack {

    private PowerConsumptionRankingsBatteryView mPowerConsumptionRankingsBatteryView;
    private int power;

    private NoLeakHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new NoLeakHandler(this);
        mPowerConsumptionRankingsBatteryView = (PowerConsumptionRankingsBatteryView) findViewById(R.id.mPowerConsumptionRankingsBatteryView);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 0, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void callBack(Message msg) {
        switch (msg.what) {
            case 0:
                mPowerConsumptionRankingsBatteryView.setLevelHeight(power += 5);
                if (power == 100) {
                    power = 0;
                }
                if (power < 30) {
                    mPowerConsumptionRankingsBatteryView.setLowerPower();
                } else if (power < 60) {
                    mPowerConsumptionRankingsBatteryView.setOffline();
                } else {
                    mPowerConsumptionRankingsBatteryView.setOnline();
                }
                break;
            default:
                break;
        }
    }

    private static class NoLeakHandler<T extends BaseHandlerCallBack> extends Handler {
        private WeakReference<T> wr;

        public NoLeakHandler(T t) {
            wr = new WeakReference<>(t);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            T t = wr.get();
            if (t != null) {
                t.callBack(msg);
            }
        }
    }
}


```
BaseHandlerCallBack 定义的接口
```
package com.oyp.battery;

import android.os.Message;

public interface BaseHandlerCallBack {
    public void callBack(Message msg);
}
```

## 3.5 效果
![这里写图片描述](https://img-blog.csdn.net/20180615162931331?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxNDQ2MjgyNDEy/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


# 四、github源代码

https://github.com/ouyangpeng/PowerConsumptionRankingsBatteryView

------

>作者：欧阳鹏 欢迎转载，与人分享是进步的源泉！ 
转载请保留原文地址：https://blog.csdn.net/ouyang_peng/article/details/80372805
>
>

####如果本文对您有所帮助，欢迎您扫码下图所示的支付宝和微信支付二维码对本文进行打赏。
![这里写图片描述](https://img-blog.csdn.net/20170413233715262?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvb3V5YW5nX3Blbmc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
