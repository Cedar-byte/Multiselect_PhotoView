package com.gxy.weixinfri;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.gxy.weixinfri.photopreview.PhotoPreviewIntent;
import com.yongchun.library.view.ImageSelectorActivity;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private Context context = EditActivity.this;
    private GridView gridView;
    private ArrayList<String> images = new ArrayList<>();// 赋值给适配器的集合
    private ArrayList<String> images01 = new ArrayList<>();// 初始的存放图片路径的集合
    private ArrayList<String> images02 = new ArrayList<>();// 后面再选择的图片路径集合
    private EditAdapter adapter;
    private Button send;// 发送
    private boolean isCancel = false;// 判断是否是点击取消后跳转过来的
    private ArrayList<String> images03 = new ArrayList<>();// 在主界面点击取消后传递过来的集合
    private Bundle extras;
    private boolean isFirst = false;// 判断是否是第一次跳转进入编辑界面
    private static final int REQUEST_PREVIEW_CODE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
        initListener();
        extras = getIntent().getExtras();
        images01 = (ArrayList<String>) getIntent().getSerializableExtra("extraImages");// 获取初始从主界面传递过来的数据
        if(images01 != null){
            for (int i = 0; i < images01.size(); i++) {
                String s = images01.get(i);
                images.add(s);
            }
            if (images.size() < 9) {
                images.add("camera_default");
            }
            adapter.notifyDataSetChanged();
        }
        images03 = extras.getStringArrayList("imagesEdit");// 获取从主界面点击取消后传递过来的数据
        if(images03 != null){
            for (int i = 0; i < images03.size(); i++) {
                String s = images03.get(i);
                images.add(s);
            }
            if (images.size() < 9) {
                images.add("camera_default");
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void init() {
        send = (Button) findViewById(R.id.send);
        gridView = (GridView) findViewById(R.id.gridview_main);
        adapter = new EditAdapter(this,images);
        gridView.setAdapter(adapter);
    }

    private void initListener() {
        /**
         * GridView点击事件
         */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = images.get(position);
                // 如果当前点击的是"add"图片 则跳转到图片选择界面
                if (path.contains("default") && position == images.size() -1 && images.size() -1 != 9) {
                    int size = images.size();
                    if(size == 0){
                        ImageSelectorActivity.start(EditActivity.this,9,1,true,true,false);// 只能选择9张图片
                    }else if(size > 0){
                        ImageSelectorActivity.start(EditActivity.this,9 - (images.size() - 1),1,true,true,false);// 只能选择9 - (images.size - 1)张
                    }
                }else{
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(context);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(images);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
//                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    /** 为activity的切换添加动画 */
                    int version = Integer.valueOf(android.os.Build.VERSION.SDK);
                    if(version > 5){
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                }
            }
        });
        /**
         * 发送 点击事件  回到MainActivity(模拟全景界面)，通过Broadcast返回并传递相应数据
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.SCENE_BROADCAST_ACTION);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isFromEdit",true);// 代表是从EditActivity跳转过来的
                bundle.putStringArrayList("images",images);
                intent.putExtras(bundle);
                sendBroadcast(intent);
                EditActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            // 获取图片选择界面返回的图片路径
            images02 = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // 将再选择的图片路径集合添加到初始集合中
            if(images02 != null){
                // 先将初始集合中的"add"图片移除掉
                for (int i = 0; i < images.size(); i++) {
                    String path = images.get(i);
                    if(path.contains("camera_default")) {
                        images.remove(images.size() - 1);
                    }
                }
                // 再将新集合的数据添加到初始集合中
                images.addAll(images02);
                // 再判断添加完成后的集合size是否小于9   如果是则继续显示"add"图
                if(images.size() < 9){
                    images.add("camera_default");
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
