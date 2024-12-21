package com.example.xchange.Login;
import com.example.xchange.MainActivity.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.R;
import com.example.xchange.Register.RegisterActivity;
import com.example.xchange.User;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText usernameEditText, passwordEditText;
    TextView signUpTextview;
    String username;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize ViewModel with Factory
        LoginViewModelFactory factory = new LoginViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signUpTextView = findViewById(R.id.sign_up);

        // Observe LiveData
        viewModel.getLoginSuccess().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "Login Successful: " + user.getUsername(), Toast.LENGTH_SHORT).show();

                // Pass the username to the next activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER", user);
                startActivity(intent);
                finish(); // Close LoginActivity
            } else {
                Toast.makeText(this, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
            }
        });



        viewModel.getLoginFailure().observe(this, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        // Handle Login Button Click
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.loginAsXChanger(username, password); // You can replace with loginAsAdmin if needed
            }
        });
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent); // Redirect to RegisterActivity
        });

    }
}
