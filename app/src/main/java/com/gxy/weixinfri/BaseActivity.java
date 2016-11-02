package com.gxy.weixinfri;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener{

    Context c;// 上下文对象
    private boolean screenHorizontal;  //是否允许横屏
    private TextView textView, textView2;
    private RelativeLayout relativeLayout, relativeLayout2;
    protected Toast mToast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(screenHorizontal){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        AppManager.getInstance().addActivity(this);// 将本Activity添加到堆栈中
        c = this;

        // 设置界面
        int layoutId = getLayoutId();

        if (layoutId != -1) {
            setContentView(layoutId);
        }

        // 2.初始化数据
        initData();

        // 3.初始化控件（M）
        initView();

        // 4.为控件注册监听器(C)
        bindView();

    }

    /**
     * 布局文件ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 定义初始化数据的抽象方法
     */
    protected abstract void initData();

    /**
     * 定义初始化界面的抽象方法
     */
    protected abstract void initView();

    /**
     * 定义数据和控件发生关系的抽象方法
     */
    protected abstract void bindView();

    /**
     * 设置是否允许横屏
     * @param screenHorizontal
     */
    public void setScreenHorizontal(boolean screenHorizontal) {
        this.screenHorizontal = screenHorizontal;
    }

    /**
     * 定义控件的点击事件
     * @param v
     */
    protected abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    protected void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    protected void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

}
