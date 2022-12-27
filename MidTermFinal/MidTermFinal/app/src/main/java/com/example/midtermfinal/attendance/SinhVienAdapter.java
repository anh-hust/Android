package com.example.midtermfinal.attendance;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.midtermfinal.R;

import java.util.List;

public class SinhVienAdapter extends ArrayAdapter {
    //sudung layoutnay
    Activity context;
    //
    int resource;
    //ds hien rhi
    List<SinhVien> objects;
    public SinhVienAdapter(@NonNull Activity context, int resource, @NonNull List<SinhVien> objects) {
        super(context, resource, objects);
        this.objects=objects;
        this.context=context;
        this.resource=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //load file xml
        LayoutInflater layoutInflater=this.context.getLayoutInflater();

        convertView = layoutInflater.inflate(this.resource, null);

        TextView tensv=convertView.findViewById(R.id.tensv);
        TextView mssv=convertView.findViewById(R.id.txt_mssv);
        CheckBox check =convertView.findViewById(R.id.checksv);
        TextView vang=convertView.findViewById(R.id.txtvang);
        SinhVien sinhvien=this.objects.get(position);
        tensv.setText(sinhvien.getTen());
        mssv.setText(sinhvien.getMssv());
        vang.setText("v: "+String.valueOf(sinhvien.getSobuoinghi()));
        // khi click checkbox
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //goi  array vitri ben diemdanh
                if (!check.isChecked()) {
                    diemdanh.vitri.add(position);
                }else {
                    for (int i=0;i<diemdanh.vitri.size();i++ ){
                        if(position==diemdanh.vitri.get(i)){ diemdanh.vitri.remove(i);}
                    }
                }
            }
        });
        return convertView;//super.getView(position, convertView, parent);
    }


}
