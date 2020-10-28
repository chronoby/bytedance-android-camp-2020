package com.example.myapplication1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button1);
        switch1 = (Switch)super.findViewById(R.id.switch1);

        final TextView tv = findViewById(R.id.tv_title);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WhichText = !WhichText;
                if(WhichText)
                    tv.setText(txt1);
                else
                    tv.setText(txt2);
                Log.i("MainActivity", "onClick: button");
            }
        });

        switch1.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    tv.setText(txt3);
                }
                else
                {
                    tv.setText(txt1);
                }
                Log.i("MainActivity", "onChange: switch");
            }
        });
    }

    boolean WhichText = false;
    boolean WhichImg = false;
    String txt1 = "Hello, bytedance!";
    String txt2 = "Hello, world!";
    String txt3 = "Hello, ZJU!";
}
