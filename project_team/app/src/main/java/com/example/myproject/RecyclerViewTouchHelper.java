package com.example.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.Adapter.ToDoAdapter;


//this class to handle swiping left and right to edit and delete task
public class RecyclerViewTouchHelper extends ItemTouchHelper.SimpleCallback {


    /*
    this class handle swiping and dragging   so it have two override functions
    on move to handle drag we don't implement it coz we don't need it
    but we use swipe left and right so we need to implement on swipe function as showed below
    */

    //we create instance from recyclerview adapter
    private ToDoAdapter adapter;


    //this constructor take instance from database to treat with it
    public RecyclerViewTouchHelper(ToDoAdapter adapter) {
        super(0, /* define directions of swipe */ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    //this function to implement drag but we don't use it
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


    //this function to implement swipe left and right
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        //this variable to take position of item swiped and store it
        final int position = viewHolder.getAdapterPosition();

        //this code to determine if swipe lift or right then handle  its action
        if (direction == ItemTouchHelper.RIGHT){

           /*
               if swiped right  , create dialog  that have a title
               then it show a question for user and maybe yes or no
               if yes , call delete task from recyclerview

               */
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are You Sure ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                //this function handle action of yes click
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deletTask(position);
                }
            });
            //this function handle action of no click
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        //this line handle swipe left
        else
        {
            adapter.editItem(position);
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
