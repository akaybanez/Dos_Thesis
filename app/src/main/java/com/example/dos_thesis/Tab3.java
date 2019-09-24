package com.example.dos_thesis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Tab3 extends Fragment {
    GridView simpleGrid;
    int images[] = {R.drawable.drop, R.drawable.cover, R.drawable.hold};

    //private RecyclerView rv_before;
    private RecyclerView rv;
    private GuideAdapter adapter;
    private ArrayList<GuideSteps> guideStepsArrayList;
    //private ArrayList<GuideSteps> afterArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        simpleGrid = rootView.findViewById(R.id.gv);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), images);
        simpleGrid.setAdapter(customAdapter);

        rv = rootView.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        guideStepsArrayList = new ArrayList<>();
        guideStepsArrayList.add(new GuideSteps("PREPARING FOR AN EARTHQUAKE", " "));
        guideStepsArrayList.add(new GuideSteps("Be familiar with dangerous spots inside offices/rooms", " "));
        guideStepsArrayList.add(new GuideSteps("Fix objects that may cause harm during an earthquake", "Faulty wiring, leaky gas connection, etc."));
        guideStepsArrayList.add(new GuideSteps("Identify safe places indoors and outdoors", "Refer to map"));
        guideStepsArrayList.add(new GuideSteps("Have a stock of emergency supplies", " "));
        guideStepsArrayList.add(new GuideSteps("Have an evacuation & reunion plan", " "));
        guideStepsArrayList.add(new GuideSteps("AFTER AN EARTHQUAKE", ""));
        guideStepsArrayList.add(new GuideSteps("Evacuate the building", "Move to a safety area"));
        guideStepsArrayList.add(new GuideSteps("Check for injuries", " "));
        guideStepsArrayList.add(new GuideSteps("Check for:", "Fire and Gas Leaks, Cracks in the walls"));
        guideStepsArrayList.add(new GuideSteps("Tune in to local radio stations for reports", " "));

        adapter = new GuideAdapter(guideStepsArrayList);
        rv.setAdapter(adapter);

        return rootView;
    }
}
