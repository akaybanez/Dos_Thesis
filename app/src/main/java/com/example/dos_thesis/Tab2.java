package com.example.dos_thesis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tab2 extends Fragment {

    GridView simpleGrid;
    int logos[] = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);


        simpleGrid = rootView.findViewById(R.id.gv);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), logos);
        simpleGrid.setAdapter(customAdapter);

        Toast.makeText(getContext(), "Swipe left", Toast.LENGTH_SHORT).show();

        return rootView;
    }
}
