package com.kaminski.gothan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaminski.gothan.R;
import com.kaminski.gothan.model.Ocurrence;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

    private List<Ocurrence> ocurrenceList;
    private Context context;

    public Adapter(List<Ocurrence> ocurrenceList, Context context) {
        this.ocurrenceList = ocurrenceList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_list_acurrences, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ocurrence ocurrence = ocurrenceList.get(position);
        holder.icon.setImageResource(R.mipmap.ic_launcher);
        holder.type.setText(ocurrence.getType());
        holder.description.setText(ocurrence.getDescription());
    }

    @Override
    public int getItemCount() {
        return ocurrenceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView icon;
        TextView description;
        TextView type;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.imageViewOcurence);
            type = itemView.findViewById(R.id.textViewTypeOcurrence);
            description = itemView.findViewById(R.id.textViewDescriptionOcurrence);
        }
    }
}
