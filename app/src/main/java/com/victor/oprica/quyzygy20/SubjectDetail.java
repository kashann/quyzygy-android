package com.victor.oprica.quyzygy20;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.oprica.quyzygy20.Model.Subject;

public class SubjectDetail extends AppCompatActivity {
    TextView subject_name, subject_description;
    ImageView subject_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String subjectId = "";
    FirebaseDatabase database;
    DatabaseReference subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);

        // firebase
        database = FirebaseDatabase.getInstance();
        subjects = database.getReference("Subjects");

        // Init view
        subject_description = findViewById(R.id.subject_description);
        subject_name = findViewById(R.id.subject_name);
        subject_image = findViewById(R.id.img_subject);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleColor(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        // Get Subject Id from Intent
        if(getIntent() != null)
            subjectId = getIntent().getStringExtra("SubjectId");
        if(!subjectId.isEmpty()){
            getDetailSubject(subjectId);
        }
    }

    private void getDetailSubject(String subjectId){
        subjects.child(subjectId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Subject subject = dataSnapshot.getValue(Subject.class);

                // Set Image
                Picasso.with(getBaseContext()).load(subject.getImage())
                        .into(subject_image);
                collapsingToolbarLayout.setTitle(subject.getName());
                subject_name.setText(subject.getName());
                subject_description.setText(subject.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
