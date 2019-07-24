package com.victor.oprica.quyzygy20.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.victor.oprica.quyzygy20.Interface.ItemClickListener;
import com.victor.oprica.quyzygy20.R;

public class CategorytestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView categorytest_name;
    public ImageView categorytest_image;

    private ItemClickListener itemClickListener;

    public CategorytestViewHolder(View itemView) {
        super(itemView);

        categorytest_name = (TextView)itemView.findViewById(R.id.menu_name_categorytestTV);
        categorytest_image = (ImageView)itemView.findViewById(R.id.menu_image_categorytestIV);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onCLick(v,getAdapterPosition(),false);
    }
}
