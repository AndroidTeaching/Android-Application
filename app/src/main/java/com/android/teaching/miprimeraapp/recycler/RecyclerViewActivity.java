package com.android.teaching.miprimeraapp.recycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.android.teaching.miprimeraapp.R;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView myRecycler;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        myRecycler = findViewById(R.id.recycler_view);

        // Asignar Layout Manager
        RecyclerView.LayoutManager myLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        myRecycler.setLayoutManager(myLayoutManager);

        // Asignar adapter
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(
                getResources().getStringArray(R.array.colors)
        );
        myRecycler.setAdapter(myRecyclerViewAdapter);
    }
}











