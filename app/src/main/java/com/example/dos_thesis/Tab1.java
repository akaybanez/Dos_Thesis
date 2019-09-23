package com.example.dos_thesis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Tab1 extends Fragment {

    private List<ListData> lData;
    private RecyclerView rv;
    private MyAdapter adapter;

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);

        searchView = rootView.findViewById(R.id.searchView);

        rv = rootView.findViewById(R.id.recycle_view);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("data").orderByKey().limitToFirst(100);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lData = new ArrayList<>();
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        ListData l = ds.getValue(ListData.class);
                        lData.add(l);
                    }
                }

                adapter = new MyAdapter(lData);
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null){
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        return rootView;
    }
}
