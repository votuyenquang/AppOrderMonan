package vku.vtq.appordermonan.Admin;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import vku.vtq.appordermonan.R;
import vku.vtq.appordermonan.adapter.XoaSuaMonAnAdapter;
import vku.vtq.appordermonan.model.MonAn;

import java.util.ArrayList;

public class XoaSuaMonAn extends AppCompatActivity {
    RecyclerView recyclerView;
    XoaSuaMonAnAdapter adapter;
    ArrayList<MonAn> arrayList;
    LinearLayoutManager layoutManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xoa_sua_mon_an);

        recyclerView = (RecyclerView) findViewById(R.id.rcl_sua_xoa_monan);
        layoutManager  = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar_suaxoa);



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        arrayList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MonAn");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    MonAn monAn = data.getValue(MonAn.class);
                    monAn.setId(data.getKey());
                    arrayList.add(monAn);
                }
                adapter = new XoaSuaMonAnAdapter(arrayList,getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(vku.vtq.appordermonan.Admin.XoaSuaMonAn.this,"Load data thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
