package com.example.midterm.info;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.midterm.R;
import com.example.midterm.data.GroupData;
import com.example.midterm.data.GroupInfo;

/**
 * TODO Activity of layout Group Detail, fragment manipulate
 * */

public class GroupDetailActivity extends AppCompatActivity {
    private GroupInfo groupInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            int position = bundle.getInt("GroupDetail_id");

            groupInfo = new GroupData().groupIntroArrayList().get(position);

            GroupDetailFragment fragment = new GroupDetailFragment();

            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.groupDetailContainer, fragment)
                    .commit();
        }
    }
}
