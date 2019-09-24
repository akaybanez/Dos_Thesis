package com.example.dos_thesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
     List<ListData> listData;
     List<ListData> listData2 = new ArrayList<>();

    public MyAdapter(List<ListData> listData) {
        this.listData = listData;
        this.listData2 = listData;
    }

    @NonNull
    @Override
    public com.example.dos_thesis.MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.dos_thesis.MyAdapter.ViewHolder holder, int position) {
        ListData ld = listData.get(position);
        holder.txtDate.setText(ld.getDatetime());
        holder.txtLocation.setText(ld.getLocation());
        holder.txtMagnitude.setText(ld.getMag());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtDate, txtLocation, txtMagnitude;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.datetimeTxt);
            txtLocation= itemView.findViewById(R.id.locationTxt);
            txtMagnitude = itemView.findViewById(R.id.magTxt);

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


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String charString = constraint.toString();

                if (charString.isEmpty()){
                    listData = listData2;
                }else{

                    List<ListData> filterList = new ArrayList<>();

                    for (ListData data : listData2){
                        if (data.getDatetime().toLowerCase().contains(charString)){
                            filterList.add(data);
                        }
                        if (data.getLocation().toLowerCase().contains(charString)){
                            filterList.add(data);
                        }
                        if (data.getMag().toLowerCase().contains(charString)){
                            filterList.add(data);
                        }
                    }

                    listData = filterList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listData;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                listData = (List<ListData>) results.values;
                notifyDataSetChanged();
            }
        };

    }

}