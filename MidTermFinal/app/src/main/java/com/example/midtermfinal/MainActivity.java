package com.example.midtermfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.midtermfinal.attendance.AttendanceFragment;
import com.example.midtermfinal.databinding.ActivityMainBinding;
import com.example.midtermfinal.group.GroupFragment;
import com.example.midtermfinal.home.HomeFragment;
import com.example.midtermfinal.progress.ProgressFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFrag(new HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeNav:
                    replaceFrag(new HomeFragment());
                    break;
                case R.id.attendanceNav:
                    replaceFrag(new AttendanceFragment());
                    break;
                case R.id.progressNav:
                    replaceFrag(new ProgressFragment());
                    break;
                case R.id.groupNav:
                    replaceFrag(new GroupFragment());
                    break;
            }
            return true;
        });
    }

    private void replaceFrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_main, fragment);
        fragmentTransaction.commit();
    }
}