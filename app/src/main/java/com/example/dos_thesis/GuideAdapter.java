package com.example.dos_thesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {
    private ArrayList<GuideSteps> dataList;

    public GuideAdapter(ArrayList<GuideSteps> dataList) {
        this.dataList = dataList;
    }

    @Override
    public GuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.guide_data, parent, false);
        return new GuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuideViewHolder holder, int position) {
        holder.text1.setText(dataList.get(position).getText1());
        holder.text2.setText(dataList.get(position).getText2());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class GuideViewHolder extends RecyclerView.ViewHolder {

        TextView text1, text2;

        GuideViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.txt1);
            text2 = itemView.findViewById(R.id.txt2);
        }
    }
}
