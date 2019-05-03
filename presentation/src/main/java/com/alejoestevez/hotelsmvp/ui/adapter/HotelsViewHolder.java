package com.alejoestevez.hotelsmvp.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alejoestevez.hotelsmvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

//Vistas a Cargar del recyclerView
public class HotelsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view)
    CardView card_view;

    @BindView(R.id.card_image)
    ImageView card_image;

    @BindView(R.id.card_title)
    TextView card_title;

    @BindView(R.id.card_location)
    TextView card_location;

    @BindView(R.id.card_rating)
    RatingBar card_rating;

    public HotelsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
