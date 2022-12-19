package com.example.midterm.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.midterm.R;
import com.example.midterm.data.ToDoItem;

import java.util.List;

public class ToDoAdapter extends ArrayAdapter<ToDoItem> {
    private Context context;
    private List<ToDoItem> toDoItemArrayList;

    public ToDoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ToDoItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.toDoItemArrayList = objects;
    }

    @Override
    public int getCount() {
        return toDoItemArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToDoItem toDoItem = toDoItemArrayList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.todo_list_item, null);

        CheckBox itemCheckBox = view.findViewById(R.id.itemCheckBox);

        itemCheckBox.setText(toDoItemArrayList.get(position).getItemContent());
        itemCheckBox.setChecked(toDoItemArrayList.get(position).isCompleted());

        return view;
    }


}
