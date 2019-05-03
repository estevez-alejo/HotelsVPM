package com.alejoestevez.hotelsmvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.alejoestevez.hotelsmvp.domain.model.Hotel;
import com.alejoestevez.hotelsmvp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelsListAdapter extends RecyclerView.Adapter<HotelsViewHolder> implements Filterable {

    private List<Hotel> items;
    private List<Hotel> allItems;
    //Filtro de Registros
    private Filter fRecords;
    private Context mContext;

    public HotelsListAdapter(List<Hotel> hotelList, Context context) {
        this.items = hotelList;
        this.allItems = hotelList;
        this.mContext = context;
    }

    //Usamos el template de card_view_hotel para representar cada elemento de la lista
    @Override
    public HotelsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_view_hotel, parent, false);
        return new HotelsViewHolder(v);
    }

    //Asociamos cada elemento a cada vista del template.
    @Override
    public void onBindViewHolder(HotelsViewHolder holder, int position) {
        final Hotel hotel = this.items.get(position);

        holder.card_title.setText(hotel.getName());
        holder.card_rating.setRating(hotel.getRating().floatValue());

        holder.card_location.setText(new StringBuilder()
                .append(hotel.getTown())
                .append(" (")
                .append(hotel.getCountry())
                .append(")")
        );

        //Cargamos mediante Picasso, la url de una imagen, en su respectivo ImageView.
        Picasso.with(mContext).load(hotel.getPhotoUrl()).into(holder.card_image);
    }

    public Hotel getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    //Retornamos la clase de filtrado RecordFilter
    @Override
    public Filter getFilter() {
        if (fRecords == null) fRecords = new RecordFilter();
        return fRecords;
    }

    private class RecordFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            //En caso de no haber valor a buscar, No es necesario el filtrado.
            if (constraint == null || constraint.length() == 0) {
                //Asignamos al resultado, todos los valores por defecto y su número de elementos.
                results.values = allItems;
                results.count = allItems.size();

            }//Sino...Debemos filtrar
            else {
                //Buscamos aquellos elementos que contengan la cadena a buscar.
                ArrayList<Hotel> fRecords = new ArrayList<>();
                for (Hotel s : allItems)
                    if (s.getName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim()))
                        fRecords.add(s);
                //Asignamos al resultado, todos los valores filtrados
                results.values = fRecords;
                results.count = fRecords.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //Asignamos los resultados del filtrado al RecyclerView y refrescamos el adaptador.
            items = (ArrayList<Hotel>) results.values;
            notifyDataSetChanged();
        }
    }


}
