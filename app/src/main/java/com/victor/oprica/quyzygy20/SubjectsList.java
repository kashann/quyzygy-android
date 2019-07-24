package com.victor.oprica.quyzygy20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.victor.oprica.quyzygy20.Interface.ItemClickListener;
import com.victor.oprica.quyzygy20.Model.Subject;
import com.victor.oprica.quyzygy20.ViewHolder.SubjectViewHolder;

public class SubjectsList extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference subjectList;
    String categoryId = "";
    FirebaseRecyclerAdapter<Subject, SubjectViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_list);

        // Firebase
        database = FirebaseDatabase.getInstance();
        subjectList = database.getReference("Subjects");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_subjects);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null){
            loadListSubjects(categoryId);
        }

    }

    private void loadListSubjects(String categoryId){
        adapter = new FirebaseRecyclerAdapter<Subject, SubjectViewHolder>(Subject.class,
                R.layout.subject_item, SubjectViewHolder.class,
                subjectList.orderByChild("MenuId").equalTo(categoryId)) {

            @Override
            protected void populateViewHolder(SubjectViewHolder viewHolder, Subject model, int position) {
                viewHolder.subject_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.subject_image);

                final Subject local = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onCLick(View view, int position, boolean isLongClick) {
                        // Start new Activity
                        Intent subjectDetail = new Intent(SubjectsList.this, SubjectDetail.class);
                        subjectDetail.putExtra("SubjectId", adapter.getRef(position).getKey()); // send subject id to new activity
                        startActivity(subjectDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
