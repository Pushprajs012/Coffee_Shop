package com.ps.coffeeshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ps.coffeeshop.AdapterAndHolder.CartAdapter;
import com.ps.coffeeshop.Application;
import com.ps.coffeeshop.Data.CartData;
import com.ps.coffeeshop.R;

import com.ps.coffeeshop.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityCartBinding binding;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth auth;
    private CartAdapter adapter;
    private List<CartData> cartList;
    private int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding= ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataFromFirebase();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartList=new ArrayList<>();
        adapter=new CartAdapter(cartList,auth.getCurrentUser().getUid(),firebaseDatabase);
        binding.recyclerView.setAdapter(adapter);
    }


    private void init(){
        firebaseDatabase=((Application) getApplicationContext()).getFirebaseDatabase();
        auth=((Application) getApplicationContext()).getFirebaseAuth();
        binding.buybtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.buybtn){
            Intent intent=new Intent(CartActivity.this, CheckoutActivity.class);
            intent.putExtra("totalPrice",totalPrice);
            startActivity(intent);
        }
    }

    private void fetchDataFromFirebase(){
        firebaseDatabase.getReference("Users").child(auth.getCurrentUser().getUid()).child("Cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    CartData cartData = dataSnapshot.getValue(CartData.class);
                    cartList.add(cartData);
                }
                adapter.notifyDataSetChanged();

                if (cartList.isEmpty()){
                    binding.ivEmptyCart.setVisibility(View.VISIBLE);
                }
                else {
                    binding.ivEmptyCart.setVisibility(View.GONE);}
                totalPrice=0;
                updateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateTotalPrice() {


        // Loop through the cart items and add the price of each item
        for (CartData cartItem : cartList) {
            totalPrice += cartItem.getPrice()*cartItem.getQuantity();
        }

        // Update the button text with the total price
        binding.buybtn.setText("Buy: $" + totalPrice);
        if (totalPrice==0){
            binding.buybtn.setText("No Items in Cart");
            binding.buybtn.setEnabled(false);
        }
    }
}