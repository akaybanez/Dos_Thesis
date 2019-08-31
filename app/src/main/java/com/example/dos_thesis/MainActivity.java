package com.example.dos_thesis;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*private TextView mTextView;
    private Button mButton;
    private EditText mEditText;*/

    /*
    // Firebase variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataBaseRef;*/

    private List<ListData> listData;
    private RecyclerView rv;
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();

        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("data");

        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot mySnap : dataSnapshot.getChildren()) {
                        ListData l = mySnap.getValue(ListData.class);
                        listData.add(l);
                    }
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        /*
        // Initialize the views
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);
        mEditText = findViewById(R.id.editText);

        // Initialize the Firebase Realtime Databse variables
        mDatabase = FirebaseDatabase.getInstance();
        mDataBaseRef = mDatabase.getReference().child("test");

        // Set the onClickListener
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = mEditText.getText().toString();

                mDataBaseRef.setValue(s);
            }
        });

        // Database listener
        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue(String.class);

                setTextView(s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setTextView(databaseError.toString());
            }
        });*/

    }

    /*private void setTextView(String s) {
        mTextView.setText(s);
    }*/

}