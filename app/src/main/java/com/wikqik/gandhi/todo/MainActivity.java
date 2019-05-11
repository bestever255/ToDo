package com.wikqik.gandhi.todo;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    ListView lstTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        lstTask = findViewById(R.id.lstTask);

        LoadTaskList();


    }

    private void LoadTaskList() {

        ArrayList<String> taskList = dbHelper.getTaskList();
        if(adapter == null){

            adapter = new ArrayAdapter<String>(this , R.layout.rgx , R.id.task_title);
            lstTask.setAdapter(adapter);

        }else{

            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu , menu);

        Drawable icon = menu.getItem(0).getIcon();
        icon.mutate();
        icon.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_task:

                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add new Task")
                        .setMessage("What do you want to do next")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String task = String.valueOf(taskEditText.getText());
                                dbHelper.insertNewTask(task);
                                LoadTaskList();

                            }
                        })
                        .setNegativeButton("Cancel" , null)
                        .create();

                dialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        LoadTaskList();
    }
}
