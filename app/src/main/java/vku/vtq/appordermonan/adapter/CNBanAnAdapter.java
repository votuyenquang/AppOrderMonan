package vku.vtq.appordermonan.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import vku.vtq.appordermonan.Admin.SuaBanAn;
import vku.vtq.appordermonan.R;
import vku.vtq.appordermonan.model.BanAn;

import java.util.ArrayList;

public class CNBanAnAdapter extends RecyclerView.Adapter<vku.vtq.appordermonan.adapter.CNBanAnAdapter.ViewHolder> {
    ArrayList<BanAn> banAns;
    Context context;
    String window;

    public CNBanAnAdapter(ArrayList<BanAn> banAns, Context context) {
        this.banAns = banAns;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_capnhatbanan,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tenbanan.setText(banAns.get(position).getTenBanAn());
        if(banAns.get(position).isTinhtrang()==true){
            holder.anhbanan.setImageResource(R.drawable.banan);
            holder.anhbanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Khách đã đi?");
                    builder.setView(R.layout.luachondatban);

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Button btn_khong = dialog.findViewById(R.id.btn_khong_dat_ban);
                    Button btn_co = dialog.findViewById(R.id.btn_co_datban);

                    btn_khong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BanAn");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(banAns.get(position).getId()).exists()){
                                        reference.child(banAns.get(position).getId()).child("tinhtrang").setValue(false);
                                        dialog.cancel();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            });

        }else if(banAns.get(position).isTinhtrang()==false){
            holder.anhbanan.setImageResource(R.drawable.banoff);
            window = Manifest.permission.SYSTEM_ALERT_WINDOW;
            holder.anhbanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Có khách hàng đã đặt bàn này?");
                    builder.setView(R.layout.luachondatban);

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    Button btn_khong = dialog.findViewById(R.id.btn_khong_dat_ban);
                    Button btn_co = dialog.findViewById(R.id.btn_co_datban);

                    btn_khong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    btn_co.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BanAn");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(banAns.get(position).getId()).exists()){
                                        reference.child(banAns.get(position).getId()).child("tinhtrang").setValue(true);
                                        dialog.cancel();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            });


        }
        holder.btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("BanAn");
                myRef.child(banAns.get(position).getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(context,"Xoá bàn ăn thành công",Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return banAns.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenbanan;
        ImageView anhbanan;
        Button btn_sua,btn_xoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenbanan = (TextView) itemView.findViewById(R.id.tenbanan_u);
            anhbanan = (ImageView) itemView.findViewById(R.id.anh_ban_an_u);
            btn_sua = (Button) itemView.findViewById(R.id.btn_suabanan);
            btn_xoa = (Button) itemView.findViewById(R.id.btn_xoabanan);


            btn_sua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SuaBanAn.class);
                    intent.putExtra("SUABANAN",banAns.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });
        }
    }
}
