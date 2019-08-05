package com.panashematsaudza.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends  RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    ArrayList<TravelDeal> deals;

    public  DealAdapter(){

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        deals  = FirebaseUtil.mdeals;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("deal",  td.getTitle());
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabaseReference.addChildEventListener(mChildEventListener);
    }
    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row ,parent,false);
        return  new DealViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal  deal = deals.get(position);
        holder.bind(deal);

    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public  class DealViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView  tv_title ,tvDescription ,tvPrice ;
        ImageView imageDeal;


        public  DealViewHolder(View itemView){
            super(itemView);
          tv_title = itemView.findViewById(R.id.tv_title);
          tvDescription = itemView.findViewById(R.id.tv_description);
          tvPrice = itemView.findViewById(R.id.tv_Price);
          imageDeal = itemView.findViewById(R.id.imageDeal);
          itemView.setOnClickListener(this);

        }

        public void bind(TravelDeal deal){
            tv_title.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImage());


        }

        @Override
        public void onClick(View view) {
            int position  = getAdapterPosition();
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(),MainActivity.class);
            intent.putExtra("Deal",selectedDeal);
            view.getContext().startActivity(intent);

        }



        private  void showImage(String url)
        {
            if (url != null && url.isEmpty()== false){
                Picasso.get().load(url)
                        .resize(160,180)
                        .centerCrop()
                        .into(imageDeal);

            }
        }
    }


}