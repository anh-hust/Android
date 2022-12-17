package com.example.midterm.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.midterm.R;

import java.util.List;

/**
 * TODO Adapter to serve Array of Group Information and pass it to ListView layout by setListAdapter
 * */

public class GroupInfoArrayAdapter extends ArrayAdapter<GroupInfo> {
    private Context context;
    private List<GroupInfo> groupInfoList;

    public GroupInfoArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GroupInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.groupInfoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GroupInfo groupInfo = groupInfoList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.group_list_item, null);

        ImageView imageView = view.findViewById(R.id.groupImgIntro);
        imageView.setImageResource(groupInfo.getGroupImageId(context)); // show the Image of group information

        TextView textView = view.findViewById(R.id.groupNameIntro);
        textView.setText(groupInfo.getGroupName());

        return view;
    }
}