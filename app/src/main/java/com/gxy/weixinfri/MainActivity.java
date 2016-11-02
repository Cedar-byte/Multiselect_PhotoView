package com.gxy.weixinfri;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btn, cancel;
    private Bundle bundle;
    private boolean isFromEdit = false;// 是否是从EditActivity跳转过来的
    private ArrayList<String> imagesEdit = new ArrayList<>();// 从EditActivity传递过来的集合数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cancel = (Button) findViewById(R.id.cancel);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 照片最大数，1 多选模式，2 单选模式，是否显示拍照图片，true，是否跳裁剪
                ImageSelectorActivity.start(MainActivity.this,9,1,true,true,false);// 跳转到图片选择界面
            }
        });

        // 创建广播接收器(从EditActivity点击发送后跳转过来)
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Constants.SCENE_BROADCAST_ACTION);
        MainActivity.this.registerReceiver(forPictureReceiver,intentfilter);


        // 点击取消时跳转到EditActivity
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putBoolean("isCancel",true);// 代表是通过点击"取消"后跳转过来的
                b.putStringArrayList("imagesEdit",imagesEdit);// 点击取消时跳转到编辑界面，并把之前从编辑界面传递过来的集合数据再传回去
                Intent i = new Intent();
                i.setClass(MainActivity.this,EditActivity.class);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            // 获取图片选择界面返回的图片路径
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            Intent i = new Intent();
            i.setClass(MainActivity.this,EditActivity.class);
            i.putExtra("extraImages",images);// 从初始界面跳转到编辑界面   并把返回的图片集合传递过去
            startActivity(i);
        }
    }

    private BroadcastReceiver forPictureReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            bundle = intent.getExtras();
            if(bundle != null){
                isFromEdit = bundle.getBoolean("isFromEdit");
                // 如果是从EditActivity跳转过来的
                if(isFromEdit){
                    // 则隐藏选择照片的按钮，显示取消按钮
                    btn.setVisibility(View.GONE);
                    cancel.setVisibility(View.VISIBLE);
                    // 并获取从EditActivity传递过来的照片集合数据
                    imagesEdit = bundle.getStringArrayList("images");
                    // 将集合中的"add"图片移除掉
                    for (int i = 0; i < imagesEdit.size(); i++) {
                        String path = imagesEdit.get(i);
                        if(path.contains("camera_default")) {
                            imagesEdit.remove(imagesEdit.size() - 1);
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(forPictureReceiver);
    }
}
