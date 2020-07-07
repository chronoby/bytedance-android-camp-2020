package com.example.chapter2;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "MainActivity onCreate");
        initView();
    }

    private void initView() {

        findViewById(R.id.btn_recyclerview).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "MainActivity onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "MainActivity onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "MainActivity onStop");
        Toast.makeText(this, "onStop", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MainActivity onDestroy");
        Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recyclerview:
                Intent intent5 = new Intent(this, RecyclerViewActivity.class);
                startActivity(intent5);
                break;
        }
    }

    private static final int REQUEST_CODE_1 = 1357;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    public int count(View root) {
        int viewCount = 0;
        if (null == root) {
            return 0;
        }
        if (root instanceof ViewGroup) {
            viewCount++;
            for (int i = 0; i < ((ViewGroup) root).getChildCount(); i++) {
                View view = ((ViewGroup) root).getChildAt(i);
                if (view instanceof ViewGroup) {
                    viewCount += count(view);
                } else {
                    viewCount++;
                }
            }
        }
        return viewCount;
    }


}