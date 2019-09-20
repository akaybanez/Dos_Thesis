package com.example.dos_thesis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Tab3 extends Fragment {

    GridView simpleGrid;
    int logos[] = {R.drawable.one, R.drawable.two, R.drawable.three};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        simpleGrid = rootView.findViewById(R.id.gv);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), logos);
        simpleGrid.setAdapter(customAdapter);

        return rootView;
    }
}
