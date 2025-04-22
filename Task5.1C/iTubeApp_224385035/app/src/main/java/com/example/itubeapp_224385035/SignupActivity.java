package com.example.itubeapp_224385035;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp_224385035.database.AppDatabase;
import com.example.itubeapp_224385035.database.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextFullName, editTextUsername, editTextPassword, editTextConfirmPassword;
    private Button buttonCreateAccount;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize components
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        executorService = Executors.newSingleThreadExecutor();

        // Set create account button click event
        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Validate input
                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user
                registerUser(fullName, username, password);
            }
        });
    }

    private void registerUser(String fullName, String username, String password) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                User existingUser = db.userDao().getUserByUsername(username);

                if (existingUser != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignupActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }

                User newUser = new User(fullName, username, password);
                long userId = db.userDao().insert(newUser);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userId > 0) {
                            Toast.makeText(SignupActivity.this, "Registration successful, please login", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 