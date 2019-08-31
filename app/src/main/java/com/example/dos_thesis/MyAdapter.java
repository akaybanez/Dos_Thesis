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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListData ld = listData.get(position);
        holder.txtDate.setText(ld.getDatetime());
        holder.txtLoc.setText(ld.getLocation());
        holder.txtMag.setText(ld.getMagnitude());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtDate, txtLoc, txtMag;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.dateTxt);
            txtLoc = itemView.findViewById(R.id.locTxt);
            txtMag = itemView.findViewById(R.id.magTxt);
        }
    }
}
