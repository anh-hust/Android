package com.example.midterm.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.midterm.R;
import com.example.midterm.data.GroupData;
import com.example.midterm.data.GroupInfo;

/**
 * TODO Fragment View handler
 * */

public class GroupDetailFragment extends Fragment {
    private GroupInfo groupInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("GroupDetail_id");

            /* Take data for detail fragment */
            groupInfo = new GroupData().groupDetailArrayList().get(position);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_detail_fragment, container, false);

        /* Set up (assign value) for Front end */
        if (groupInfo != null) {
            TextView groupName = view.findViewById(R.id.groupName);
            groupName.setText(groupInfo.getGroupName());

            ImageView groupImage = view.findViewById(R.id.groupImage);
            groupImage.setImageResource(groupInfo.getGroupImageId(getActivity()));

            TextView groupTopic = view.findViewById(R.id.groupTopic);
            groupTopic.setText(groupInfo.getGroupTopic());

            String detailString = "";
            for (int i = 0; i < groupInfo.getGroupMembers().toArray().length; i++) {
                detailString += String.format("%s.  %s \n%-50s %s\n",
                        String.valueOf(i + 1),
                        groupInfo.getGroupMembers().get(i),
                        "",
                        groupInfo.getGroupMembersID().get(i));
            }
            TextView groupDetail = view.findViewById(R.id.groupDetail);
            groupDetail.setText(detailString);
        }

        return view;
    }


}