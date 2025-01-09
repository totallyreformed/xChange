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

import com.example.xchange.Notification;
import com.example.xchange.R;
import com.example.xchange.Register.RegisterActivity;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText usernameEditText, passwordEditText;
    private TextView signUpTextView;
    private String username;
    private User user;

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
        signUpTextView = findViewById(R.id.sign_up);

        // Observe LiveData
        viewModel.getLoginSuccess().observe(this, user -> {
            if (user != null) {
                this.user = user;
                this.username = user.getUsername();
                Toast.makeText(this, "Login Successful: " + user.getUsername(), Toast.LENGTH_SHORT).show();
                proceedToMainActivity(user); // Navigate to MainActivity and pass notifications
            } else {
                Toast.makeText(this, "Unexpected error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe LiveData for login failure
        viewModel.getLoginFailure().observe(this, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.loginUser(username, password);
            }
        });

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent); // Redirect to RegisterActivity
        });
    }

    private void proceedToMainActivity(User user) {
        viewModel.getNotificationsForUser(user.getUsername(), new UserRepository.NotificationCallback() {
            @Override
            public void onSuccess(List<Notification> notifications) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER", user);
                intent.putParcelableArrayListExtra("NOTIFICATIONS", new ArrayList<>(notifications)); // Pass notifications
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(String message) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USER", user);
                intent.putParcelableArrayListExtra("NOTIFICATIONS", new ArrayList<>()); // Pass empty list on failure
                startActivity(intent);
                finish();
            }
        });
    }
}
