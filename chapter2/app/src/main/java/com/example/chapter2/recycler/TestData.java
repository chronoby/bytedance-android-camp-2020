package com.example.chapter2.recycler;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TestData {
    String id;
    String cont;
    String time;

    public TestData(String title, String hot, String time) {
        this.id = title;
        this.cont = hot;
        this.time = time;
    }
}
