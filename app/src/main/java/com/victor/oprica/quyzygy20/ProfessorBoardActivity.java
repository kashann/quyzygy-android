package com.victor.oprica.quyzygy20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.victor.oprica.quyzygy20.entities.Quiz;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfessorBoardActivity extends AppCompatActivity {
    private SharedPreferences keyPreferences;
    private LinearLayout quizzesll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_board);

        quizzesll = findViewById(R.id.quizzesLL);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            keyPreferences = getSharedPreferences("keyPrefs", MODE_PRIVATE);
            String URL = "http://" + getString(R.string.ip) + ":8080/myQuizzes?sk=" + keyPreferences.getString("sk", "0");

            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Quiz[] results = new Gson().fromJson(response, Quiz[].class);

                    for (int i = 0; i < results.length; i++){

                        final TextView tv = new TextView(getApplicationContext());

                        String s = "Quiz #" + results[i].ID;
                        s += "\nName: \"" + results[i].QuizName + "\"";
                        s += "\nCourse: \"" + results[i].CourseName + "\"";
                        s += "\nDuration: " + results[i].Duration / 60 + " minutes";

                        tv.setText(s);

                        quizzesll.addView(tv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        // ACTIVATE
                        Button act_Btn = new Button(getApplicationContext());
                        act_Btn.setText("Activate");
                        act_Btn.setTag(results[i].ID);

                        quizzesll.addView(act_Btn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        act_Btn.setOnClickListener(new Button.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                v.setEnabled(false);
                                activateQuiz(Integer.parseInt(v.getTag().toString()), tv);
                                Toast.makeText(getApplicationContext(), "Quiz activated!", Toast.LENGTH_LONG).show();
                            }
                        });

                        // START
                        Button startBtn = new Button(getApplicationContext());
                        startBtn.setText("Start");
                        startBtn.setTag(results[i].ID);

                        quizzesll.addView(startBtn, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        startBtn.setOnClickListener(new Button.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                v.setEnabled(false);
                                startQuiz(Integer.parseInt(v.getTag().toString()));
                                Toast.makeText(getApplicationContext(), "Quiz started!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }, new Response.ErrorListener() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void activateQuiz(int id, final TextView textView){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://" + getString(R.string.ip) + ":8080/activateQuiz?sk=" + keyPreferences.getString("sk", "") + "&quizID=" + id;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        textView.setText(textView.getText() + "\nACCESS CODE: " + jsonObject.getInt("AccessCode"));
                    }catch (JSONException err){
                        Log.d("Error", err.toString());
                    }
                }
            }, new Response.ErrorListener() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startQuiz(int id){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://" + getString(R.string.ip) + ":8080/startQuiz?sk=" + keyPreferences.getString("sk", "") + "&quizID=" + id;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateToCreateQuiz(View view){
        Intent explicitIntent = new Intent(ProfessorBoardActivity.this, CreateQuizActivity.class);
        startActivity(explicitIntent);
    }

    public void navigateToMainActivity(View view){
        Intent explicitIntent = new Intent(ProfessorBoardActivity.this, MainActivity.class);
        startActivity(explicitIntent);
    }
}
