package com.example.midterm.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.midterm.R;
import com.example.midterm.data.GroupInfo;

import java.util.List;

public class GroupProgressArrayAdapter extends ArrayAdapter<GroupInfo> {
    private Context context;
    private List<GroupInfo> groupNameList;

    public GroupProgressArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GroupInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.groupNameList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GroupInfo groupInfo = groupNameList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.progress_list_item, null);

        TextView groupID = view.findViewById(R.id.groupID);
        groupID.setText(groupInfo.getGroupName());

        Log.d("Progress Adapter", "Number of Completed: " + String.valueOf(groupInfo.getNumberOfCompletedItems()));
        Log.d("Progress Adapter", "Number of Items: " + String.valueOf(groupInfo.getNumberOfItems()));
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(groupInfo.getNumberOfItems());
        progressBar.setProgress(groupInfo.getNumberOfCompletedItems());

        TextView numberOfItems = view.findViewById(R.id.numberOfItems);
        numberOfItems.setText(String.valueOf(groupInfo.getNumberOfItems()) + " items");


        return view;
    }
}
