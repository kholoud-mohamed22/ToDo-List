package com.example.myproject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.Adapter.ToDoAdapter;
import com.example.myproject.Model.ToDoModel;
import com.example.myproject.Utils.DataBaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListner {


    // we create objects for view to can implement here items in home activity
    private RecyclerView mRecyclerview;
    private FloatingActionButton fab;
    private SearchView searchView;

    //we create objects for classes to can use it
    private DataBaseHelper myDB;
    private ToDoAdapter adapter;

    //this is arraylist for model to take date and then retrieve it
    private List<ToDoModel> mList;



    //this function to implement ui of tis activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this line to hide actionbar
        getSupportActionBar().hide();

        //this code to implement elements on main activity
        mRecyclerview = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.searchView);


        //this lines to allocate instances for classes we created
        myDB = new DataBaseHelper(MainActivity.this);
        mList = new ArrayList<>();
        adapter = new ToDoAdapter(myDB , MainActivity.this);


        //this line to create instance from item touch helper to use touch helper class
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adapter));

        //then connect touch helper with recyclerview to use it
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        //this line to connect between recycler adapter and database adapter
        mRecyclerview.setAdapter(adapter);

        searchView.clearFocus();

        //this function implement search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });



        mRecyclerview.setHasFixedSize(true);

        //this line to define style of elements  of recyclerview
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));


        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);


        //this function to handle fab button where pressing
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager() , AddNewTask.TAG);
            }
        });

    }



    //this code to implement search box
    private void filterList(String text) {
        List<ToDoModel> modelList = new ArrayList<>();
        for (ToDoModel model : mList){
            if (model.getTask().toLowerCase().contains(text.toLowerCase())){
                modelList.add(model);
            }
        }
        if (modelList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setModelList(modelList);

        }
    }


    //this code to implement interface
    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adapter.setTasks(mList);
        adapter.notifyDataSetChanged();
    }
}