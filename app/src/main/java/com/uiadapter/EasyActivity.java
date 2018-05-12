package com.uiadapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.R;
import com.easy.fragment.ClassTableFragment;
import com.easy.fragment.ConsumptionFragment;
import com.easy.fragment.GradeFragment;
import com.easy.fragment.RobClassFragment;
import com.easy.fragment.SchoolCardFragment;

public class EasyActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private String type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.ui_adapter_easy_activity);

        Intent intent = getIntent();
        toolbar = (Toolbar) findViewById(R.id.toolbar);




        type = intent.getStringExtra("type");

        //"roboclass","schoolcard","schoolconsume","grade","class"

        if (type.equals("roboclass")) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fl_content, new RobClassFragment(), "roboclass").
                    commit();
            type = "自动选课";
        }else if(type.equals("schoolcard")){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fl_content, new SchoolCardFragment(), "schoolcard").
                    commit();
            type = "校园卡充值";
        }else if(type.equals("schoolconsume")){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fl_content, new ConsumptionFragment(), "schoolconsume").
                    commit();
            type = "消费记录查询";
        }else if(type.equals("grade")){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fl_content, new GradeFragment(), "grade").
                    commit();
            type = "成绩查询";
        }else if(type.equals("class")){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fl_content, new ClassTableFragment(), "class").
                    commit();
            type = "课表查看";
        }else if(type.equals("")){

        }

        InitToolBar();

    }

    public void InitToolBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(type!=null){
            actionBar.setTitle(type);
        }
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

}
