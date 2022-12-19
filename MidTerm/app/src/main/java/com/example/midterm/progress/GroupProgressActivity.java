package com.example.midterm.progress;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.midterm.R;
import com.example.midterm.data.GroupData;
import com.example.midterm.data.GroupInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

/**
 * TODO Activity handle for Group Progress Management, fragment manipulate
 */

public class GroupProgressActivity extends AppCompatActivity {
    private GroupInfo groupInfo;
    public static String TODO_COLLECTION = "TODO_JOB";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_progress);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            int position = bundle.getInt("GroupProgress_id");

            groupInfo = new GroupData().groupProgressArrayList().get(position);

            ToDoListFragment fragment = new ToDoListFragment();

            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.groupProgressContainer, fragment)
                    .commit();
        }
    }
}
