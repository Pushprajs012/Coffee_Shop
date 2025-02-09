package com.ps.coffeeshop.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.ps.coffeeshop.Application;
import com.ps.coffeeshop.Data.User;
import com.ps.coffeeshop.R;
import com.ps.coffeeshop.databinding.ActivityRegistrationBinding;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityRegistrationBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        firebaseAuth=((Application) getApplicationContext()).getFirebaseAuth();
        db=((Application) getApplicationContext()).getFirebaseDatabase();
        binding.btnRegister.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnRegister){
            if (checkVelidation()){
                registerwithfirebase();
            }
        }
        else if (v.getId()==R.id.btnLogin){
            onBackPressed();
        }
    }

    private void registerwithfirebase() {
        String email=binding.etEmail.getText().toString().trim();
        String password=binding.etPassword.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(  task -> {
            if (task.isSuccessful()) {
                addDatatoFirestore(firebaseAuth.getCurrentUser().getUid());
                startActivity(new Intent(this, ProductActivity.class));
                finish();

            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkVelidation() {
        if (binding.etFullName.getText().toString().isEmpty()){
            binding.etFullName.setError("Enter Full Name");
            return false;
        }
        else if (binding.etMobile.getText().toString().isEmpty() || binding.etMobile.getText().toString().length()!=10){
            binding.etMobile.setError("Enter Valid Mobile Number");
            return false;
        }
        else if (binding.etEmail.getText().toString().isEmpty()){
            binding.etEmail.setError("Enter Email");
            return false;
        }
       else if (!isValidEmail(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError("Enter a valid Gmail address");
            return false;
        }
        else if (binding.etAddress.getText().toString().isEmpty()){
            binding.etAddress.setError("Enter Address");
        }
        else if (binding.etPinCode.getText().toString().isEmpty() || !binding.etPinCode.getText().toString().matches("[A-Z][0-9][A-Z][0-9][A-Z][0-9]")) {
            binding.etPinCode.setError("Enter a valid PIN code with all text in uppercase");
            return false;

        } else if (binding.etPassword.getText().toString().isEmpty() || binding.etPassword.getText().toString().length() < 6) {
            binding.inputPassword.setError("Enter Password");
            return false;

        }

        return true;
    }

    private boolean isValidEmail(String email) {
        // Regex to validate email and ensure it ends with @gmail.com
        String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }

    private void addDatatoFirestore(String uuid){
        String fullName = binding.etFullName.getText().toString().trim();
        String mobile = binding.etMobile.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String address = binding.etAddress.getText().toString().trim();
        String pinCode = binding.etPinCode.getText().toString().trim();

        User user = new User(fullName, mobile, email, address, pinCode);

        db.getReference("Users").child(uuid).child("User").setValue(user);
    }
}