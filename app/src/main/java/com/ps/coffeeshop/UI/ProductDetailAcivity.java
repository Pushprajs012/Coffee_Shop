package com.ps.coffeeshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.coffeeshop.Application;
import com.ps.coffeeshop.Data.CartData;
import com.ps.coffeeshop.R;
import com.ps.coffeeshop.databinding.ActivityProductDetailAcivityBinding;

public class ProductDetailAcivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityProductDetailAcivityBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private CartData cartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailAcivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        selectSize(binding.sizeSmall, binding.sizeMedium, binding.sizeLarge);
        getandsetDatatoView();
        checkdataincart();
        binding.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void getandsetDatatoView() {

        firebaseDatabase.getReference("drinks").child(getIntent().getStringExtra("coffeedata")).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                cartData=new CartData(task.getResult().child("product_id").getValue(String.class),
                        task.getResult().child("name").getValue(String.class),
                        task.getResult().child("price").getValue(Integer.class),
                        task.getResult().child("size").getValue(String.class),
                        task.getResult().child("image").getValue(String.class),
                       task.getResult().child("description").getValue(String.class),
                       1);

                binding.productName.setText(task.getResult().child("name").getValue(String.class));
                binding.priceText.setText("$"+task.getResult().child("price").getValue(Integer.class).toString());
                binding.abouttext.setText(task.getResult().child("description").getValue(String.class));
               // binding.productSize.setText(task.getResult().child("size").getValue(String.class));

                Glide.with(binding.ivcoffee.getContext()).load(task.getResult().child("image").getValue(String.class)).into(binding.ivcoffee);

            }
        });
    }


    private void init() {
        firebaseDatabase = ((Application) getApplicationContext()).getFirebaseDatabase();
        auth = ((Application) getApplicationContext()).getFirebaseAuth();
        binding.addtocart.setOnClickListener(this);
        binding.sizeLarge.setOnClickListener(this);
        binding.sizeMedium.setOnClickListener(this);
        binding.sizeSmall.setOnClickListener(this);
        binding.back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
if (v.getId()== R.id.addtocart){

    firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).child("Cart").child(getIntent().getStringExtra("coffeedata")).setValue(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
           startActivity(new Intent(ProductDetailAcivity.this, CartActivity.class));
        }
    });

    if (v.getId()==R.id.back){
        onBackPressed();
    }

}

if (v.getId()==R.id.size_small){
    selectSize(binding.sizeSmall, binding.sizeMedium, binding.sizeLarge);
}
if (v.getId()==R.id.size_medium){
    selectSize(binding.sizeSmall, binding.sizeMedium, binding.sizeLarge);
}
if (v.getId()==R.id.size_large){
    selectSize(binding.sizeSmall, binding.sizeMedium, binding.sizeLarge);
}


    }

    private void checkdataincart(){
        firebaseDatabase.getReference("Users")
                .child(auth.getCurrentUser().getUid())
                .child("Cart")
                .child(getIntent().getStringExtra("coffeedata"))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            binding.addtocart.setText("Already In Cart");
                            binding.addtocart.setEnabled(false);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void selectSize(TextView selected, TextView... others) {
        // Set selected TextView
        selected.setSelected(true);
        selected.setBackgroundResource(R.drawable.size_button_background);

        // Unselect other TextViews
        for (TextView other : others) {
            other.setSelected(false);
            other.setBackgroundResource(R.drawable.size_button_background);
        }
    }
}