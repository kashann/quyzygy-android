package com.victor.oprica.quyzygy20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtResultScore, getTxtResultQuestion;
    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference questions_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        questions_score = database.getReference("Question_Score");

        txtResultScore = findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = findViewById(R.id.txtTotalQuestion);
        btnTryAgain = findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this, CategorytestActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Get data from bundle and set to view
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int score = extra.getInt("SCORE");
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");

            txtResultScore.setText(String.format("SCORE : %d", score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d", correctAnswer,totalQuestion));

           // progressBar.setMax(totalQuestion);
           // progressBar.setProgress(correctAnswer);

            // Upload point to DB
            //questions_score.child(String.format("%s_%s", Common))
        }
    }
}
