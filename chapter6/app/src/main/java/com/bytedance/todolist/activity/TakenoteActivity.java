package com.bytedance.todolist.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;

import java.util.Date;

public class TakenoteActivity extends AppCompatActivity {
    private static final String TAG = "TAG";

    private Button mBtn;
    private EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_note_layout);


        mBtn = findViewById(R.id.btn);
        mText = findViewById(R.id.textInput);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence content = mText.getText();
                if(TextUtils.isEmpty(content)) {
                    Toast.makeText(TakenoteActivity.this, "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TakenoteActivity.this).todoListDao();
                        dao.addTodo(new TodoListEntity(content.toString(), new Date(System.currentTimeMillis())));
                    }
                }.start();
                TakenoteActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
