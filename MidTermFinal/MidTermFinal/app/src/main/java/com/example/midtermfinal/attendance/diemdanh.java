package com.example.midtermfinal.attendance;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.midtermfinal.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link } factory method to
 * create an instance of this fragment.
 */
public class diemdanh extends Fragment implements AdapterView.OnItemSelectedListener {

    ListView dsHienThi;
    ArrayList<SinhVien> dsSinhVien;


    SinhVienAdapter svAdapter;
    Button btnsave;
    Button btnchtietvangtheongay;
    Button btnclearday;
    Button btnreset;
    Spinner spinnerdate;
    ArrayList<String> datediemdanh;
    // tao 1 list luu vi tri khi click checkbox
    public static ArrayList<Integer> vitri =new ArrayList<>();
    //se;ect day
    public static String setdayed;
    public ArrayList<SinhVien> dsvangtheongay=new ArrayList<>();
    ArrayList<SinhVien> dsvangtheongay_reset_clearday;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addcontrols();


    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_diemdanh, container, false);
        btnsave=view.findViewById(R.id.btn_save);
        btnchtietvangtheongay=view.findViewById(R.id.chtietvangtheongay);
        btnclearday=view.findViewById(R.id.btnclealdate);
        btnreset=view.findViewById(R.id.btnclearall);
        spinnerdate=view.findViewById(R.id.spinnerdate);
        spinnerdate.setOnItemSelectedListener(this);
        String[] date=getResources().getStringArray(R.array.datetime);
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,date);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerdate.setAdapter(arrayAdapter);
        //datediemdanh=loadDate("ngaydiemdanh"); doc tu file
        datediemdanh=Readday(); //doc tu database
        dsHienThi=view.findViewById(R.id.listdiemdanh);
       // dsSinhVien=loaddata("com/example/midterm/diemdanh");//doc tu file
        dsSinhVien=Readdata("diemdanh");//doc tu database
        svAdapter= new SinhVienAdapter(
                getActivity(),R.layout.single_sv,dsSinhVien
        );
        dsHienThi.setAdapter(svAdapter);

        addevent();
        return view;

    }


    private void addevent() {
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder arlert=new AlertDialog.Builder(getActivity());
                arlert.setTitle("Xac nhan!");
                arlert.setMessage("ban muon xoa toan bo diem danh?");
                arlert.setCancelable(false);
                arlert.setIcon(R.drawable.ic_twotone_add_alarm_24);
                arlert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        for (int i=0;i<dsSinhVien.size();i++){
                            dsSinhVien.get(i).setSobuoinghi(0);
                        }
                        svAdapter.notifyDataSetChanged();

                        ArrayList<SinhVien> svnull=new ArrayList<>();
                        for (int i=0;i<datediemdanh.size();i++){
                            writedata("com/example/midterm/vang"+datediemdanh.get(i),svnull);//xoa toan bo danh sach vang
                            deletedata("vang"+datediemdanh.get(i));
                        }
                        datediemdanh.clear();

                        writedate("ngaydiemdanh",datediemdanh); //xoa toan bo ngay da diem danh
                        deletedata("ngaydiemdanh");

                        writedata("com/example/midterm/diemdanh",dsSinhVien);//update lai so ngay nghi
                        Updatedata("diemdanh",dsSinhVien);

                        Toast.makeText(getActivity(),"da reset",Toast.LENGTH_SHORT).show();
                    }
                });
                arlert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                arlert.show();

            }
        });
        btnclearday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder arlert=new AlertDialog.Builder(getActivity());
                arlert.setTitle("Xac nhan!");
                arlert.setMessage("Ban muon xoa diem danh ngay: "+setdayed);
                arlert.setCancelable(false);
                arlert.setIcon(R.drawable.ic_twotone_add_alarm_24);
                arlert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        dsvangtheongay_reset_clearday=loaddata("com/example/midterm/vang"+setdayed);//lay danh sach vang theo ngay
                        dsvangtheongay_reset_clearday=Readdata("vang"+setdayed);//lay tu database
                        for (int i=0;i<dsvangtheongay_reset_clearday.size();i++){
                            for (int j=0;j<dsSinhVien.size();j++){
                                if (dsvangtheongay_reset_clearday.get(i).getMssv()==dsSinhVien.get(j).getMssv()){
                                    int count=dsSinhVien.get(j).getSobuoinghi();
                                    if (count>0) dsSinhVien.get(j).setSobuoinghi(count-1);
                                }
                            }
                        }
                        svAdapter.notifyDataSetChanged();
                        dsvangtheongay_reset_clearday.clear();

                        writedata("com/example/midterm/vang"+setdayed,dsvangtheongay_reset_clearday);// xoa danh sach nghi
                        deletedata("vang"+setdayed);
                        datediemdanh.remove(setdayed);

                        writedate("ngaydiemdanh",datediemdanh);//xoa ngay
                        deletedata("ngaydiemdanh"); //xoa toan bo ngay
                        ImportDay(datediemdanh);//them moi
                        writedata("com/example/midterm/diemdanh",dsSinhVien);
                        Updatedata("diemdanh",dsSinhVien);
                        Toast.makeText(getActivity(),"Da xoa diem danh ngay :"+setdayed,Toast.LENGTH_SHORT).show();
                    }
                });
                arlert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                arlert.show();

            }
        });
        btnchtietvangtheongay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new vangtheongay();
                FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frag_main,fragment).commit();

            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vitri.isEmpty()){
                    for(int k:vitri){
                        int count=dsSinhVien.get(k).getSobuoinghi();
                        dsSinhVien.get(k).setSobuoinghi(count+1);
                        dsvangtheongay.add(dsSinhVien.get(k));


                        // Toast.makeText(getActivity(),"so buoi nghi: "+String.valueOf(dsSinhVien.get(k).getSobuoinghi()),Toast.LENGTH_LONG).show();
                    }
                }
                vitri.clear();

                btnsave.setVisibility(View.GONE);

                datediemdanh.add(setdayed);
                writedate("ngaydiemdanh",datediemdanh);
                deletedata("ngaydiemdanh");//xoa du lieu cu
                ImportDay(datediemdanh);//them du lieu moi
                Toast.makeText(getActivity(),"Da diem danh"+setdayed,Toast.LENGTH_SHORT).show();
                writedata("com/example/midterm/diemdanh",dsSinhVien);

                //Importdata("diemdanh",dsSinhVien);
                Updatedata("diemdanh",dsSinhVien);
                writedata("com/example/midterm/vang"+setdayed,dsvangtheongay);
                Importdata("vang"+setdayed,dsvangtheongay);
                dsvangtheongay.clear();

               svAdapter.notifyDataSetChanged();
            }

        });

    }

        /** ham chuyen du lieu sinh vien vao file*/
    private void writedata(String s, ArrayList<SinhVien> dsSinhVien) {
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Gson gson=new Gson();
        String json=gson.toJson(dsSinhVien);
        editor.putString(s,json);
        editor.apply();

    }

    /**
     *
     * @param s :ten file
     * @return list<SInhVien></>
     */
    private ArrayList<SinhVien> loaddata(String s){
        ArrayList<SinhVien> arrayList=new ArrayList<>();
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString(s,null);
        Type type=new TypeToken<ArrayList<SinhVien>>(){}.getType();
        arrayList=gson.fromJson(json,type);
        if (arrayList==null){
            arrayList =new ArrayList<>();
        }
        return arrayList;
    }

    /**
     * ham ghi lai nhung ngay da diem danh
     * @param filename
     * @param date
     */
    private void writedate(String filename, ArrayList<String> date){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Gson gson=new Gson();
        String json=gson.toJson(date);
        editor.putString(filename,json);
        editor.apply();
    }

    /**
     *
     * @param filename
     * @return mang luu ngay diem danh
     */
    private ArrayList<String> loadDate(String filename){
        ArrayList<String > arrayList;
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString(filename,null);
        Type type=new TypeToken<ArrayList<String>>(){}.getType();
        arrayList=gson.fromJson(json,type);
        if (arrayList==null){
            arrayList=new ArrayList<>();
        }
        return arrayList;
    }

    /**
     * ham lay thoi gian ngay hom nay
     * @return chuoi string ngay thang nam
     */



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId()==R.id.spinnerdate){
            String value =adapterView.getItemAtPosition(i).toString();
            setdayed=value;

            btnsave.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void Importdata(String filename,ArrayList<SinhVien> ds){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        for (int i=0;i<ds.size();i++){
            Map<String,Object> sv=new HashMap<>();
            sv.put("ten",ds.get(i).getTen());
            sv.put("MSSV",ds.get(i).getMssv());
            sv.put("so buoi vang",ds.get(i).getSobuoinghi());
            firebaseFirestore.collection(filename).add(sv);
        }
    }




    private ArrayList<SinhVien>  Readdata(String filename){
        ArrayList<SinhVien> dssinhvien=new ArrayList<>();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection(filename).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.e("Error",error.getMessage());

                }
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if (documentChange.getType()==DocumentChange.Type.ADDED){
                        dssinhvien.add(documentChange.getDocument().toObject(SinhVien.class));
                    }
                }
            }
        });
         return dssinhvien  ;
    }
    private void deletedata(String filename){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();

        firebaseFirestore.collection(filename)
        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            String documentID = documentSnapshot.getId();

                            firebaseFirestore.collection(filename).document(documentID).delete();
                        }
                        Toast.makeText(getActivity(),"deleted database",Toast.LENGTH_SHORT).show();


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"fail deleted database",Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void Updatedata(String filename,ArrayList<SinhVien> dsSinhVien){

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        for (int i=0;i<dsSinhVien.size();i++) {
            Map<String, Object> sv = new HashMap<>();
            sv.put("ten", dsSinhVien.get(i).getTen());
            sv.put("MSSV", dsSinhVien.get(i).getMssv());
            sv.put("so buoi nghi", dsSinhVien.get(i).getSobuoinghi());
            firebaseFirestore.collection(filename).whereEqualTo("MSSV",dsSinhVien.get(i).getMssv())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String documentID = documentSnapshot.getId();
                        firebaseFirestore.collection(filename).document(documentID).update(sv);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"fail on update",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void ImportDay(ArrayList<String> date){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        for (int i=0;i<date.size();i++){
            Map<String,Object> sv=new HashMap<>();
            sv.put("date",setdayed);

            firebaseFirestore.collection("ngaydiemdanh").add(sv);
        }
    }
    private ArrayList<String> Readday(){
        ArrayList<String> dsdate=new ArrayList<>();
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("ngaydiemdanh").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.e("Error",error.getMessage());

                }
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if (documentChange.getType()==DocumentChange.Type.ADDED){
                        dsdate.add(documentChange.getDocument().toString());
                    }
                }
            }
        });
        return dsdate  ;
    }
    private void UpdateDay(ArrayList<String> dsdate){
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        for (int i=0;i<dsdate.size();i++) {
            Map<String, Object> sv = new HashMap<>();
            sv.put("date", dsSinhVien.get(i).getTen());

            firebaseFirestore.collection("ngaydiemdanh").whereEqualTo("date",dsdate.get(i))
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentID = documentSnapshot.getId();
                                firebaseFirestore.collection("ngaydiemdanh").document(documentID).update(sv);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"fail on update",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}