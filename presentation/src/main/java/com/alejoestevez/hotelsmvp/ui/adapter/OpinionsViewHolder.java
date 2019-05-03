package com.alejoestevez.hotelsmvp.ui.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alejoestevez.hotelsmvp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

//Vistas a Cargar del recyclerView
public class OpinionsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.card_view)
    CardView card_view;

    @BindView(R.id.card_message)
    TextView card_message;

    @BindView(R.id.card_creationdate)
    TextView card_creationdate;

    @BindView(R.id.card_rating)
    RatingBar card_rating;

    @BindView(R.id.card_name)
    TextView card_name;

    public OpinionsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

