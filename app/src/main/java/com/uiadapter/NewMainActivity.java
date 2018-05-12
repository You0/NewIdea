package com.uiadapter;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.R;
import com.duanqu.Idea.activity.AttentionActivity;
import com.duanqu.Idea.activity.MainActivity1;
import com.duanqu.Idea.activity.SendActivity;
import com.duanqu.Idea.config.RequestCode;
import com.duanqu.Idea.fragment.InnerViewPager;
import com.duanqu.Idea.fragment.NewSuggestViewPager;

import java.util.ArrayList;


public class NewMainActivity extends AppCompatActivity{
    public static Toolbar toolbar;
    public static NavigateTabBar mNavigateTabBar;
    public static RelativeLayout bottomBar;
    private final int SEND_IMAGE = 99;


    private ImageView      mTabMoreIv;


    private static final String TAG_PAGE_HOME    = "朋友圈";
    private static final String TAG_PAGE_CITY    = "广场";
    private static final String TAG_PAGE_MORE    = "more";
    private static final String TAG_PAGE_MESSAGE = "实验室";
    private static final String TAG_PAGE_PERSON = "我的";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uiadapter_main_activity);

        InitToolBar();
        InitBottomBar(savedInstanceState);
        //LoadContentFragment(R.id.content_main);

    }

    private void InitBottomBar(Bundle savedInstanceState) {
        bottomBar = (RelativeLayout) findViewById(R.id.bottomBar);
        this.mNavigateTabBar = (NavigateTabBar) findViewById(R.id.main_tabbar);
        this.mTabMoreIv = (ImageView) findViewById(R.id.tab_more_iv);

        this.mNavigateTabBar.onRestoreInstanceState(savedInstanceState);

        this.mNavigateTabBar.addTab(FragmentMain.class, new NavigateTabBar.TabParam(R.mipmap.navigate_tab_home, R.mipmap.navigate_tab_home_selected, TAG_PAGE_HOME));
        this.mNavigateTabBar.addTab(NewSuggestViewPager.class, new NavigateTabBar.TabParam(R.mipmap.navigate_tab_city, R.mipmap.navigate_tab_city_selected, TAG_PAGE_CITY));
        this.mNavigateTabBar.addTab(null, new NavigateTabBar.TabParam(0, 0, TAG_PAGE_MORE));
        this.mNavigateTabBar.addTab(FragmentTypeDouban.class, new NavigateTabBar.TabParam(R.mipmap.navigate_tab_message, R.mipmap.navigate_tab_message_selected, TAG_PAGE_MESSAGE));
        this.mNavigateTabBar.addTab(null, new NavigateTabBar.TabParam(R.mipmap.navigate_tab_person, R.mipmap.navigate_tab_person_selected, TAG_PAGE_PERSON));

        this.mTabMoreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "更多", Toast.LENGTH_LONG).show();
                PopMenuView.getInstance().show(NewMainActivity.this, mTabMoreIv);
            }
        });

    }

    public void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != toolbar){
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setTitle("校声");

    }

    public void LoadContentFragment(int ResourseId) {
//        getSupportFragmentManager().beginTransaction()
//                .replace(ResourseId,
//                        new FragmentMain(), "main_content").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main1,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(this, AttentionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.scale_anim, 0);

        }

        if(item.getItemId() == R.id.action_setting){
//            Intent intent = new Intent(this,SettingActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.anim_enter_from_bottom,R.anim.anim_exit_from_bottom);
        }

        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String videoFile;
        String thum[];
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.RECORDE_SHOW) {
                //RecordResult result = new RecordResult(data);
                //得到视频地址，和缩略图地址的数组，返回十张缩略图
                //videoFile = result.getPath();
                //thum = result.getThumbnail();
                //result.getDuration();

                //Toast.makeText(MainActivity1.this, thum[0], Toast.LENGTH_LONG).show();
                // tv_result.setText("视频路径:" + videoFile + "图片路径:" + thum[0]);
                //打开发送界面
                Intent intent = new Intent(this, SendActivity.class);
                intent.putExtra("id", "video");
                intent.putExtra("videoUri", data.getStringExtra("path"));
                intent.putExtra("thum", data.getStringExtra("thum"));
                startActivity(intent);
                //startUpload();//可以在这里调用上传的方法
            }

            if(requestCode == SEND_IMAGE){
                Intent intent = new Intent(this, SendActivity.class);
                intent.putExtra("id", "image");
                String image;
                ArrayList arrayList = data.getCharSequenceArrayListExtra("data_return");
                if (arrayList==null){
                    image = data.getStringExtra("data_return");
                    intent.putExtra("tag",0);
                    intent.putExtra("image",image);
                }else{
                    intent.putExtra("tag",1);
                    intent.putStringArrayListExtra("images",arrayList);
                }

                startActivity(intent);
            }
        } else {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }



}




