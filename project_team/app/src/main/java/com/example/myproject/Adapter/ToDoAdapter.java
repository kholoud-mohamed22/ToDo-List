package com.example.myproject.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.AddNewTask;
import com.example.myproject.MainActivity;
import com.example.myproject.Model.ToDoModel;
import com.example.myproject.R;
import com.example.myproject.Utils.DataBaseHelper;

import java.util.List;

// THIS class to handle adapter
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {


    //we create array list to store data
    private List<ToDoModel> mList;

    private MainActivity activity;
    //we create instance from calss Database to use functions inside it
    private DataBaseHelper myDB;


    //we create constructor to take instance from database  , and context .
    public ToDoAdapter(DataBaseHelper myDB , MainActivity activity){
        this.activity = activity;
        this.myDB = myDB;
    }


    //this function  are override to give parent and return view
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout , parent , false);
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //this line to view objects inside arraylist on recyclerview
        final ToDoModel item = mList.get(position);

        holder.mCheckBox.setText(item.getTask());
        holder.mCheckBox.setChecked(toBoolean(item.getStatus()));

        //this code to update changes on  checkbox
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //this lune to check if check box is checked or not
                    myDB.updateStatus(item.getId() , 1);
                }else
                    myDB.updateStatus(item.getId() , 0);
            }
        });
    }


    //this function to convert statue from int to boolean
    public boolean toBoolean(int num){
        return num!=0;
    }

    //this function to return context
    public Context getContext(){
        return activity;
    }

    //this function to take arraylist and assign it to recyclerview
    public void setTasks(List<ToDoModel> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    //this function to delete task from database then from recyclerview
    public void deletTask(int position){
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    //this function to edit task
    public void editItem(int position){
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task" , item.getTask());

        AddNewTask task = new AddNewTask();
        //this code to show data inside task first  then edit it
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager() , task.getTag());


    }


    //this function return size of arraylist
    @Override
    public int getItemCount() {
        return mList.size();
    }


    //this class to implement views of template or custom
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }

    public void setModelList(List<ToDoModel> modelList){
        this.mList = modelList;
        notifyDataSetChanged();
    }
}
