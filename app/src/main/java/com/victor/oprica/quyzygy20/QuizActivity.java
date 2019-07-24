package com.victor.oprica.quyzygy20;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.victor.oprica.quyzygy20.entities.Identity;
import com.victor.oprica.quyzygy20.entities.Question;
import com.victor.oprica.quyzygy20.entities.WSCPWQ;

import java.io.UnsupportedEncodingException;

public class QuizActivity extends AppCompatActivity {
    private  Question currentQuestion;
    TextView tv_time, question;
    private SharedPreferences keyPreferences;
    private Questions mQuestions = new Questions();
    private int duration;
    private int mScore = 0;
    private int mQuestionsLength = mQuestions.mQuestions.length;
    ProgressBar pb_answers, pb_time;
    LinearLayout answersLayout;
    int index, counter;
    private Identity identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        index = 0;
        counter = 0;
        keyPreferences = getSharedPreferences("keyPrefs", MODE_PRIVATE);

        identity = new Identity();
        identity.WSID = keyPreferences.getString("wsid", "");
        identity.AccessCode = Integer.parseInt(keyPreferences.getString("acc", ""));
        identity.SecretKey = keyPreferences.getString("sk", "");
        pb_answers = findViewById(R.id.pb_answers);
        pb_answers.setMax(mQuestionsLength);
        pb_time = findViewById(R.id.pb_time);
        question = findViewById(R.id.question);
        tv_time = findViewById(R.id.tv_time);
        nextQuestion();
    }

    private void gameOver(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(QuizActivity.this);
        alertDialogBuilder.setMessage("Game Over! Your score is " + mScore + " points.")
                .setCancelable(false)
                .setNegativeButton("EXIT",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void nextQuestion(){
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://" + getString(R.string.ip) + ":8080/nextQuestion?sk=" + identity.SecretKey + "&ac=" + identity.AccessCode + "&wsid=" + identity.WSID;
            Log.v("getting next question", URL);

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try{
                        Log.e("questionresponse", response);
                        if (response.contains("Completed!")){
                            Intent explicitIntent = new Intent(getApplicationContext(), StudentGradesActivity.class);
                            startActivityForResult(explicitIntent, 1);
                            Toast.makeText(getApplicationContext(), "Quiz completed!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        WSCPWQ packet = new Gson().fromJson(response, WSCPWQ.class);
                        question.setText(packet.Data.Text);
                        currentQuestion = packet.Data;
                        if(counter == 0){
                            duration = packet.Time * 1000;
                            startTimer(duration);
                        }

                        // SINGLE ANSWER
                        if (packet.Data.Type.equals("SingleAnswer")){
                            String[] answers = new Gson().fromJson(packet.Data.Answers, String[].class);

                            for (int  i = 0; i < answers.length; i++){
                                Button button = new Button(getApplicationContext());
                                button.setText(answers[i]);
                                button.setTextSize(22);

                                answersLayout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                button.setOnClickListener(new Button.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        counter++;
                                        postAnswer(((Button)v).getText().toString(),currentQuestion);
                                    }
                                });
                            }
                        }

                        // OPEN ANSWER
                        else if (packet.Data.Type.equals("OpenAnswer")){
                            final EditText et = new EditText(getApplicationContext());
                            answersLayout.addView(et, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            Button button = new Button(getApplicationContext());
                            button.setText("Answer");
                            button.setTextSize(22);
                            answersLayout.addView(button, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            button.setOnClickListener(new Button.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    counter++;
                                    postAnswer(et.getText().toString(), currentQuestion);
                                }
                            });
                        }

                        // MULTIPLE ANSWER
                        else if (packet.Data.Type.equals("MultipleAnswer")){
                            // TO BE DONE
                        }
                    }
                    catch (Exception ee){
                        Log.e("ParseError", ee.toString());
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            requestQueue.add(stringRequest);
            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(com.android.volley.Request<Object> request) {
                    requestQueue.getCache().clear();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimer(final int duration){
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                pb_time.setMax(duration);
                pb_time.setProgress(duration);
                answersLayout = findViewById(R.id.answers_Layout);
                new CountDownTimer(duration, 50) {
                    public void onTick(long millisUntilFinished) {
                        tv_time.setText("Time remaining: " +
                                millisUntilFinished / 3600000 + " hours " +
                                millisUntilFinished % 3600000 / 60000 + " minutes " +
                                millisUntilFinished % 60000 / 1000 + " seconds");
                        pb_time.setProgress((int)millisUntilFinished);
                    }

                    public void onFinish() {
                        gameOver();
                    }
                }.start();
            }
        });
    }

    private void postAnswer(String answer, Question currentQuestion){
        try {
            int earnedScore = 0;
            if(answer.equals(currentQuestion.CorrectAnswer)) {
                earnedScore = currentQuestion.Points;
                mScore += currentQuestion.Points;
            }
            final RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://" + getString(R.string.ip) + ":8080/postAnswer?sk=" + identity.SecretKey + "&ac=" + identity.AccessCode + "&wsid=" + identity.WSID;

            final String requestBody = "{\"questionID\":"+
                    currentQuestion.ID
                    + ",\"answer\":\"" + answer + "\",\"score\":" + earnedScore + ",\"total\":" + currentQuestion.Points + "}";

            StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        Log.e("questionresponse", response);
                        if (answersLayout.getChildCount() > 0)
                            answersLayout.removeAllViews();
                        nextQuestion();
                    }
                    catch (Exception ee){
                        Log.e("questionresponse", ee.toString());
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {

                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    }
                    catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(com.android.volley.Request<Object> request) {
                    requestQueue.getCache().clear();
                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}