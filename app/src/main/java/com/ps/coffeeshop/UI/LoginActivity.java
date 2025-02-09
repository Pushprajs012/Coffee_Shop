package com.ps.coffeeshop.UI;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.ps.coffeeshop.Application;
import com.ps.coffeeshop.R;
import com.ps.coffeeshop.databinding.ActivityLoginBinding;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        LinearGradient linearGradient = new LinearGradient(
                0, 0, binding.appname.getWidth(), 0,
                new int[]{getResources().getColor(R.color.orange), getResources().getColor(R.color.purple)},
                null,
                Shader.TileMode.CLAMP
        );

        binding.appname.getPaint().setShader(linearGradient);

    }

    private void init() {
        firebaseAuth = ((Application) getApplicationContext()).getFirebaseAuth();
        binding.btnLogin.setOnClickListener(this);
        binding.createaccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            if (checkVelidation()) {
            loginwithfirebase();
            }
        } else if (v.getId() == R.id.createaccount) {
            startActivity(new Intent(this, RegistrationActivity.class));

        }
    }

    private void loginwithfirebase() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                startActivity(new Intent(this, ProductActivity.class));
            }
            else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean checkVelidation() {
        if (binding.etEmail.getText().toString().isEmpty()) {
            binding.etEmail.setError("Enter Email");
            return false;
        }
        else if (!isValidEmail(binding.etEmail.getText().toString().trim())) {
            binding.etEmail.setError("Enter a valid Gmail address");
            return false;
        }
        else if (binding.etPassword.getText().toString().isEmpty() || binding.etPassword.getText().toString().length() < 6) {
            binding.passwordLayout.setError("Enter Password");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isValidEmail(String email) {
        // Regex to validate email and ensure it ends with @gmail.com
        String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        return Pattern.compile(emailPattern).matcher(email).matches();
    }
}