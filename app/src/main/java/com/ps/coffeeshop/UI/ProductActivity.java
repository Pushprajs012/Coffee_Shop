package com.ps.coffeeshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.coffeeshop.AdapterAndHolder.HomeRecycleViewAdapter;
import com.ps.coffeeshop.Application;

import com.ps.coffeeshop.Data.DrinkData;
import com.ps.coffeeshop.R;

import com.ps.coffeeshop.databinding.ActivityProductBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProductBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private HomeRecycleViewAdapter adapter;
    private List<DrinkData> hoodieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataFromFirebase();
       // binding.toolbar.setTitle(auth.getCurrentUser().getDisplayName());
        setname();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hoodieList=new ArrayList<>();
        adapter=new HomeRecycleViewAdapter(hoodieList,firebaseDatabase,auth.getCurrentUser().getUid());
        binding.recyclerView.setAdapter(adapter);
    }


    private void init(){
        firebaseDatabase= ((Application) getApplicationContext()).getFirebaseDatabase();
        auth=((Application) getApplicationContext()).getFirebaseAuth();
        binding.menucart.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.menucart){
            startActivity(new Intent(ProductActivity.this, CartActivity.class));
        }

    }

    private void fetchDataFromFirebase(){
        firebaseDatabase.getReference("drinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hoodieList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DrinkData drinkData = dataSnapshot.getValue(DrinkData.class);
                    hoodieList.add(drinkData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void setname(){
        firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).child("User").child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}