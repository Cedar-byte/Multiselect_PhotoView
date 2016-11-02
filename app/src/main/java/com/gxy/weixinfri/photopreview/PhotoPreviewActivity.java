package com.gxy.weixinfri.photopreview;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.gxy.weixinfri.R;
import java.util.ArrayList;

/**
 *  照片预览界面
 */
public class PhotoPreviewActivity extends AppCompatActivity implements PhotoPagerAdapter.PhotoViewClickListener{

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";

    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "preview_result";

    /** 预览请求状态码 */
    public static final int REQUEST_PREVIEW = 99;

    private ArrayList<String> paths;
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        initViews();

        paths = new ArrayList<>();
        ArrayList<String> pathArr = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        if(pathArr != null && pathArr.size() < 9 && pathArr.contains("camera_default")){
            pathArr.remove("camera_default");
        }
        paths.addAll(pathArr);

        currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);

        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                updateActionBarTitle();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        updateActionBarTitle();
    }

    private void initViews(){
        mViewPager = (ViewPagerFixed) findViewById(R.id.vp_photos);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.pickerToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {
        onBackPressed();
    }

    public void updateActionBarTitle() {
        getSupportActionBar().setTitle(
                getString(R.string.image_index, mViewPager.getCurrentItem() + 1, paths.size()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, paths);
        setResult(RESULT_OK, intent);
        finish();
        /** 为activity的切换添加动画 */
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
        if(version > 5){
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        // 删除当前照片
        if(item.getItemId() == R.id.action_discard){
            final int index = mViewPager.getCurrentItem();
            final String deletedPath =  paths.get(index);

            if(paths.size() <= 1){
                // 最后一张照片弹出删除提示
                // show confirm dialog
                new AlertDialog.Builder(this)
                        .setTitle("删除吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                paths.remove(index);
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }else{

                paths.remove(index);
                mPagerAdapter.notifyDataSetChanged();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
