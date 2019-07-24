package com.victor.oprica.quyzygy20;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.victor.oprica.quyzygy20.Common.Common;

public class Playing extends AppCompatActivity implements View.OnClickListener {
    final static long INTERVAL = 5000; // 1 min
    final static long TIMEOUT = 100000; // 100 sec
    int progressValue = 0;
    private CountDownTimer mCountDown;
    int index = 0, score = 0, thisQuestion=0, totalQuestion,correctAnswer;

    ProgressBar progressBar;
    ImageView questiontest_image;
    Button btnA, btnB, btnC, btnD, btnR;
    TextView txtScore, txtQuestionNum, questiontest_text;
    String subjectId = "";
    FirebaseDatabase database;
    DatabaseReference subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        // Views
        txtScore = (TextView)findViewById(R.id.txtScore);
        txtQuestionNum = (TextView)findViewById(R.id.txtTotalQuestion);
        questiontest_text = (TextView)findViewById(R.id.questiontest_textPlaying);
        //questiontest_image = (ImageView)findViewById(R.id.questiontest_imagePlaying);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnA = (Button)findViewById(R.id.btnAnswerA);
        btnB = (Button)findViewById(R.id.btnAnswerB);
        btnC = (Button)findViewById(R.id.btnAnswerC);
        btnD = (Button)findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mCountDown.cancel();
        if(index < totalQuestion) // still have question in list
        {
            Button clickedButton = (Button)view;
            if(clickedButton.getText().equals(Common.questiontestList.get(index).getCorrectAnswer()))
            {
                // Choose correct answer
                score+=10;
                correctAnswer++;
                showQuestion(++index); // next question
            }
            else {
                // Choose wrong answer
                Intent intent = new Intent(this, Done.class);
                Bundle dataSend = new Bundle();
                dataSend.putInt("SCORE",score);
                dataSend.putInt("TOTAL",totalQuestion);
                dataSend.putInt("CORRECT",correctAnswer);
                intent.putExtras(dataSend);
                startActivity(intent);
                finish();
            }
            txtScore.setText(String.format("%d",score));
        }
    }

    private void showQuestion(int i){
        if(index < totalQuestion){
            thisQuestion++;
            txtQuestionNum.setText(String.format("%d / %d",thisQuestion,totalQuestion));
            progressBar.setProgress(0);
            progressValue=0;
//            if(Common.questiontestList.get(index).getIsImageQuestion().equals("true"))
//            {
//                //if is image
//                Picasso.with(getBaseContext())
//                        .load(Common.questiontestList.get(index).getQuestion())
//                        .into(questiontest_image);
////                questiontest_image.setVisibility(View.VISIBLE);
////                questiontest_text.setVisibility(View.INVISIBLE);
//            }
//            else {
//                questiontest_text.setText(Common.questiontestList.get(index).getQuestion());
//
//                // if is text
////                questiontest_image.setVisibility(View.INVISIBLE);
////                questiontest_text.setVisibility(View.VISIBLE);
//            }

            questiontest_text.setText(Common.questiontestList.get(index).getQuestiontest());

            btnA.setText(Common.questiontestList.get(index).getAnswerA());
            btnB.setText(Common.questiontestList.get(index).getAnswerB());
            btnC.setText(Common.questiontestList.get(index).getAnswerC());
            btnD.setText(Common.questiontestList.get(index).getAnswerD());

            mCountDown.start(); // Start timer
        }
        else{
            // if it is final question
            Intent intent = new Intent(Playing.this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE",score);
            dataSend.putInt("TOTAL",totalQuestion);
            dataSend.putInt("CORRECT",correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        totalQuestion = Common.questiontestList.size();
        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                showQuestion(++index);
            }
        };
        showQuestion(index);// because this is current index
    }
}
