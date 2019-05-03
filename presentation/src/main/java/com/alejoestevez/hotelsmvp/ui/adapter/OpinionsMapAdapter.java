package com.alejoestevez.hotelsmvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alejoestevez.hotelsmvp.domain.model.Opinion;
import com.alejoestevez.hotelsmvp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class OpinionsMapAdapter extends RecyclerView.Adapter<OpinionsViewHolder> {

    private Map<String, Opinion> items;
    private Context mContext;

    public OpinionsMapAdapter(Map<String, Opinion> opinionMap, Context context) {
        this.items = opinionMap;
        this.mContext = context;
    }

    //Usamos el template de card_view_hotel para representar cada elemento de la lista
    @Override
    public OpinionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_opinion, parent, false);
        return new OpinionsViewHolder(v);
    }

    //Asociamos cada elemento a cada vista del template.
    @Override
    public void onBindViewHolder(OpinionsViewHolder holder, int position) {
        final Opinion opinion = (new ArrayList<>(this.items.values()).get(position));

        holder.card_message.setText(opinion.getMessage());

        SimpleDateFormat dateFormat = new SimpleDateFormat(mContext.getString(R.string.date_pattern), Locale.getDefault());

        holder.card_creationdate.setText(dateFormat.format(opinion.getCreationDate().getTime()));
        holder.card_rating.setRating(opinion.getRating());
        holder.card_name.setText(opinion.getName() != null ? opinion.getName() : mContext.getString(R.string.anonymous));
    }

    public Opinion getItemByUID(String uid) {
        return this.items.get(uid);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
