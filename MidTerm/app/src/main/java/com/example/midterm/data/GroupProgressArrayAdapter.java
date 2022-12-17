package com.example.midterm.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.midterm.R;

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

        TextView textView = view.findViewById(R.id.groupID);
        textView.setText(groupInfo.getGroupName());

        return view;
    }
}
