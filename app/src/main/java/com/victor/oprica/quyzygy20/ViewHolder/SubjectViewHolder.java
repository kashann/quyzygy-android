package com.victor.oprica.quyzygy20.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.oprica.quyzygy20.Interface.ItemClickListener;
import com.victor.oprica.quyzygy20.R;

public class SubjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView subject_name;
    public ImageView subject_image;

    public ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SubjectViewHolder(View itemView) {
        super(itemView);

        subject_name = (TextView) itemView.findViewById(R.id.subject_name);
        subject_image = (ImageView) itemView.findViewById(R.id.subject_image);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onCLick(view, getAdapterPosition(),false);
    }
}
