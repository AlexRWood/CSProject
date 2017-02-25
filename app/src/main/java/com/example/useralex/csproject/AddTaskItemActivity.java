package com.example.useralex.csproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * this class is used to add a task item to the ListArray<TaskItem> when on a phone.
 */

public class AddTaskItemActivity extends AppCompatActivity {
    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private Button mButtonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTextTitle = (EditText) findViewById(R.id.add_task_title);
        mEditTextDescription = (EditText) findViewById(R.id.add_task_description);
        mButtonAdd = (Button) findViewById(R.id.add_btn);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Title", mEditTextTitle.getText().toString());
                returnIntent.putExtra("Description", mEditTextDescription.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);

                // Return to parent activity in onActivityResult
                finish();
            }
        });
    }
}
