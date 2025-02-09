package com.ps.coffeeshop.AdapterAndHolder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.coffeeshop.Data.CartData;
import com.ps.coffeeshop.databinding.RowLayoutCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartHolder> {

    private List<CartData> cartList;
    private String uid;
    private FirebaseDatabase firebaseDatabase;


    public CartAdapter(List<CartData> cartData, String uid, FirebaseDatabase firebaseDatabase) {
        this.cartList = cartData;
        this.uid = uid;
        this.firebaseDatabase = firebaseDatabase;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowLayoutCartBinding binding = RowLayoutCartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        CartData cartData = cartList.get(position);

        // Binding data to views
        holder.binding.productName.setText(cartData.getName());
        holder.binding.productPrice.setText("$"+cartData.getPrice());
        holder.binding.productSize.setText(cartData.getSize());
        holder.binding.quantity.setText(String.valueOf(cartData.getQuantity()));

        Glide.with(holder.binding.productImage.getContext())
                .load(cartData.getImage())
                .into(holder.binding.productImage);

        holder.binding.decreaseButton.setOnClickListener(v -> {
            if (cartData.getQuantity() > 1) {
                cartData.setQuantity(cartData.getQuantity() - 1);
                holder.binding.quantity.setText(String.valueOf(cartData.getQuantity()));
                notifyDataSetChanged();
                firebaseDatabase.getReference("Users").child(uid).child("Cart").child(cartData.getProduct_id()).child("quantity").setValue(cartData.getQuantity());

                } else {
                cartList.remove(cartData);
                firebaseDatabase.getReference("Users").child(uid).child("Cart").child(cartData.getProduct_id()).removeValue();

                notifyDataSetChanged();
            }
        });
        holder.binding.increaseButton.setOnClickListener(v -> {
            cartData.setQuantity(cartData.getQuantity() + 1);
            holder.binding.quantity.setText(String.valueOf(cartData.getQuantity()));
            notifyDataSetChanged();

            firebaseDatabase.getReference("Users").child(uid).child("Cart").child(cartData.getProduct_id()).child("quantity").setValue(cartData.getQuantity());

        });
    }

    @Override
    public int getItemCount() {

        return cartList.size();
    }
}
