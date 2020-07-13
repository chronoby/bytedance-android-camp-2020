package com.bytedance.todolist.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.NoteOperator;
import com.bytedance.todolist.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private TextView mContent;
    private TextView mTimestamp;
    private CheckBox checkBox;
    private View deleteBtn;
    private final NoteOperator operator;

    public TodoListItemHolder(@NonNull View itemView, NoteOperator operation) {
        super(itemView);
        this.operator = operation;
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        checkBox = itemView.findViewById(R.id.checkbox);
        deleteBtn = itemView.findViewById(R.id.btn_delete);
    }

    public void bind(final TodoListEntity entity) {
        mContent.setText(entity.getContent());
        mTimestamp.setText(formatDate(entity.getTime()));

        checkBox.setOnCheckedChangeListener(null);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // CheckBox选中更新
                if(isChecked) {
                    mContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    mContent.setTextColor(Color.GRAY);
                }
                else {
                    mContent.getPaint().setFlags(0);
                    mContent.setTextColor(Color.BLACK);
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator.deleteNote(entity);
            }
        });
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
