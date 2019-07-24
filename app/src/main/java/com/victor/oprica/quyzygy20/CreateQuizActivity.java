package com.victor.oprica.quyzygy20;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CreateQuizActivity extends AppCompatActivity {
    private LinearLayout parentLinearLayout;
    SharedPreferences loginPreferences;
    EditText et_quizName, et_duration, et_courseName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        parentLinearLayout = findViewById(R.id.sv_LinearLayout);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
    }

    public void onAddQuestion(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.view_question, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDeleteQuestion(View v) {
        parentLinearLayout.removeView((View) v.getParent().getParent());
    }

    public void saveQuiz (View v){
        Log.v("0x000AAAB", "" + parentLinearLayout.getChildCount());

        for (int i = 0; i < parentLinearLayout.getChildCount(); i++){
           try{
               LinearLayout questionLayout = (LinearLayout)parentLinearLayout.getChildAt(i);
               LinearLayout headerLayout = (LinearLayout)questionLayout.getChildAt(0);
               LinearLayout _1 = (LinearLayout)questionLayout.getChildAt(1);
               LinearLayout _2 = (LinearLayout)questionLayout.getChildAt(2);
               LinearLayout _3 = (LinearLayout)questionLayout.getChildAt(3);
               LinearLayout _4 = (LinearLayout)questionLayout.getChildAt(4);
               String question = ((EditText)headerLayout.getChildAt(0)).getText().toString();
               Log.v("0x000AAAB", question);
           }
           catch(Exception e){
                Log.v("0x000AAAB", e.toString());
            }
        }
    }
}