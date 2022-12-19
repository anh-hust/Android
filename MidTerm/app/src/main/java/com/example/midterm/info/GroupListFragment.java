package com.example.midterm.info;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.midterm.R;
import com.example.midterm.adapter.GroupInfoArrayAdapter;
import com.example.midterm.data.GroupInfo;
import com.example.midterm.data.GroupData;

import java.util.List;

/**
 * TODO Fragment View handler and pass data on CLick of List View
 */

public class GroupListFragment extends ListFragment {
    private List<GroupInfo> groupIntroList;
    private groupListCallbacks groupListCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupIntroList = new GroupData().groupIntroArrayList();

        /* Pass data and set adapter */
        GroupInfoArrayAdapter groupInfoArrayAdapter = new GroupInfoArrayAdapter(getActivity(),
                R.layout.group_list_item, groupIntroList);

        setListAdapter(groupInfoArrayAdapter);

    }

    /* inflater allow us to fetch our fragment_main.xml   */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /* Put fragment_main into container */
        View view = inflater.inflate(R.layout.group_list_fragment, container, false);

        return view;
    }

    public interface groupListCallbacks {
        void onGroupInfoItemSelected(GroupInfo groupInfo, int position);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        GroupInfo groupIntro = groupIntroList.get(position);
        this.groupListCallbacks.onGroupInfoItemSelected(groupIntro, position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.groupListCallbacks = (groupListCallbacks) context;
    }
}
