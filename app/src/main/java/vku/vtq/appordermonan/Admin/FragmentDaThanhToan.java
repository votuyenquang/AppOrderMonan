package vku.vtq.appordermonan.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import vku.vtq.appordermonan.R;
import vku.vtq.appordermonan.adapter.DaThanhToanAdapter;
import vku.vtq.appordermonan.model.HoaDon;

import java.util.ArrayList;

public class FragmentDaThanhToan extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    DaThanhToanAdapter adapter;
    ArrayList<HoaDon> arrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dathanhtoan, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rcl_dathanhtoan);
        layoutManager  = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("HoaDon");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    HoaDon hoaDon = data.getValue(HoaDon.class);

                    hoaDon.setIdhoadon(data.getKey());
                    if (hoaDon.isTinhtrang()==true) {
                        arrayList.add(hoaDon);
                    }
                }
                adapter = new DaThanhToanAdapter(arrayList,getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;

    }
}
