package com.example.dos_thesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListData> listData;

    public MyAdapter(List<ListData> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public com.example.dos_thesis.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
        return new ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtDate, txtLatitude, txtLocation, txtLongitude, txtMagnitude;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.datetimeTxt);
            txtLatitude = itemView.findViewById(R.id.latitudeTxt);
            txtLocation= itemView.findViewById(R.id.locationTxt);
            txtLongitude = itemView.findViewById(R.id.longitudeTxt);
            txtMagnitude = itemView.findViewById(R.id.magnitudeTxt);
        }
    }

    @Override
    public int getItemCount() {
        int a;

        if(listData != null && !listData.isEmpty()) {
            a = listData.size();
        }
        else {
            a = 0;
        }

        return a;
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.dos_thesis.MyAdapter.ViewHolder holder, int position) {
        ListData ld = listData.get(position);
        holder.txtDate.setText(ld.getDatetime());
        holder.txtLatitude.setText(ld.getLatitude());
        holder.txtLocation.setText(ld.getLocation());
        holder.txtLongitude.setText(ld.getLongitude());
        holder.txtMagnitude.setText(ld.getMagnitude());
    }

}