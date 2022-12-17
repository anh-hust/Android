package com.example.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.midterm.data.GroupInfo;
import com.example.midterm.info.GroupDetailActivity;
import com.example.midterm.info.GroupListFragment;
import com.example.midterm.progress.GroupProgressActivity;
import com.example.midterm.progress.GroupProgressFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements GroupListFragment.groupListCallbacks, GroupProgressFragment.groupProgressCallbacks{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * VARIABLE welcomeText
        * TODO: to clear Text each Navigation
        *  */
        TextView welcomeText = findViewById(R.id.welcomeText);

        MaterialToolbar materialToolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        materialToolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            item.setChecked(true);
            drawerLayout.closeDrawer(GravityCompat.START);

            switch (id) {
                case R.id.attendance:
                    welcomeText.setText("");
                    break;
                case R.id.groupInfo:
//                        Toast.makeText(MainActivity.this, "Class Info clicked",
//                                Toast.LENGTH_SHORT).show();
                    welcomeText.setText("");
                    replaceFrag(new GroupListFragment());
                    break;
                case R.id.progress:
                    welcomeText.setText("");
                    replaceFrag(new GroupProgressFragment());
                    break;
                default:
                    return true;
            }
            return true;
        });
    }

    private void replaceFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onGroupInfoItemSelected(GroupInfo groupInfo, int position) {
        Intent intent = new Intent(MainActivity.this, GroupDetailActivity.class);
        intent.putExtra("GroupDetail_id", position);

        /* It gonna ERROR ActivityNotFoundException --> Go to Manifest add another
            <activity> name:.GroupDetailActivity */
        startActivity(intent);
    }


    @Override
    public void onGroupProgressItemSelected(GroupInfo groupInfo, int position) {
        Intent intent = new Intent(MainActivity.this, GroupProgressActivity.class);
        intent.putExtra("GroupProgress_id", position);

        /* It gonna ERROR ActivityNotFoundException --> Go to Manifest add another
            <activity> name:.GroupDetailActivity */
        startActivity(intent);
    }
}