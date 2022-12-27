package com.example.midtermfinal.attendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.midtermfinal.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@lin
 * create an instance of this fragment.
 */
public class vangtheongay extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    ListView dsHienThi;
    ArrayList<SinhVien> dsSinhVien;

    TextView txtdate;
    SinhVienAdapter svAdapter;

    public vangtheongay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    gay.
     */
    // TODO: Rename and change types and number of parameters

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_vangtheongay, container, false);
        dsHienThi=view.findViewById(R.id.dsvang);
        txtdate=view.findViewById(R.id.txtdate);
        txtdate.setText("Sinh Vien Vang : "+diemdanh.setdayed);
        dsSinhVien=loaddata("com/example/midterm/vang"+diemdanh.setdayed);
        svAdapter= new SinhVienAdapter(
                getActivity(),R.layout.single_sv,dsSinhVien
        );
        dsHienThi.setAdapter(svAdapter);
        // Inflate the layout for this fragment
        return view;
    }
    private ArrayList<SinhVien> loaddata(String s){
        ArrayList<SinhVien> arrayList=new ArrayList<>();
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString(s,null);
        Type type=new TypeToken<ArrayList<SinhVien>>(){}.getType();
        arrayList=gson.fromJson(json,type);

        return arrayList;
    }
}