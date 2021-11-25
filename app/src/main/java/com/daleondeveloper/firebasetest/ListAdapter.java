package com.daleondeveloper.firebasetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ProcedureViewHolder> {

    private Context context;
    private List<String> countries;

    public ListAdapter(Context context, List<String> countries) {
        this.context = context;
        this.countries = countries;
    }

    @NonNull
    @Override
    public ProcedureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View courseItem = LayoutInflater.from(context).inflate(R.layout.simple_list_item,parent,false);
        return  new ProcedureViewHolder(courseItem);
    }
    @Override
    public void onBindViewHolder(@NonNull ProcedureViewHolder holder, int position) {
        holder.countryName.setText(countries.get(position));

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static final class ProcedureViewHolder extends  RecyclerView.ViewHolder{

        TextView countryName;


        public ProcedureViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.simple_text_view_item);
        }
    }
}