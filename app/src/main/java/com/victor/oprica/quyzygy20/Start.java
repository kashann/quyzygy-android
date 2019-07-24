package com.victor.oprica.quyzygy20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.oprica.quyzygy20.Common.Common;
import com.victor.oprica.quyzygy20.Model.Questionstest;
import java.util.Collections;

public class Start extends AppCompatActivity {
    Button btnPlay;
    FirebaseDatabase database;
    DatabaseReference questionstest;
    String categorytestId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        database = FirebaseDatabase.getInstance();
        questionstest = database.getReference("Questionstest");

        loadQuestion(Common.categorytestId);

        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Start.this, Playing.class);
                startActivity(intent);
                finish();
            }
        });

        // Get Intent here
        if(getIntent() != null)
            categorytestId = getIntent().getStringExtra("CategorytestId");
        if(!categorytestId.isEmpty() && categorytestId != null){
            loadQuestion(categorytestId);
        }
    }

    private void loadQuestion(String categorytestId){
        // First, clear List if have old question
        if(Common.questiontestList.size() > 0)
            Common.questiontestList.clear();

        questionstest.orderByChild("MenuId").equalTo(categorytestId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            Questionstest ques = postSnapshot.getValue(Questionstest.class);
                            Common.questiontestList.add(ques);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        // Random list
        Collections.shuffle(Common.questiontestList);
    }
}
