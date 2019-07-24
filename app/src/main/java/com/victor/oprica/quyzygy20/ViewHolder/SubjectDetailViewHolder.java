package com.victor.oprica.quyzygy20.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.oprica.quyzygy20.Interface.ItemClickListener;
import com.victor.oprica.quyzygy20.R;

public class SubjectDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView subjectdetail_name;
    public ImageView subjectdetail_image;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SubjectDetailViewHolder(View itemView) {
        super(itemView);

        subjectdetail_name = (TextView) itemView.findViewById(R.id.subject_name);
        subjectdetail_image = (ImageView) itemView.findViewById(R.id.subject_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(), false);

    }
}
