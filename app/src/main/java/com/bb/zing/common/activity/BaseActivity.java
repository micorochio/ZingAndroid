package com.bb.zing.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bb.zing.BBApplication;
import com.bb.zing.common.utils.ActivityUtil;


public class BaseActivity extends AppCompatActivity {
    protected BBApplication application;
    private String TAG;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application = (BBApplication) getApplication();
        super.onCreate(savedInstanceState);
        TAG = this.getLocalClassName();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityUtil.add(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityUtil.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.remove(this);

    }

    public void showTip(String content) {
        ActivityUtil.showTip(content);
    }


}
