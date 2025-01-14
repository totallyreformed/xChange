package com.example.xchange.Register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Login.LoginActivity;
import com.example.xchange.R;
import com.example.xchange.SimpleCalendar;
import com.example.xchange.User;

/**
 * Activity class for user registration in the xChange application.
 * <p>
 * This activity provides a user interface for creating a new account. It handles input validation,
 * user registration logic, and navigation to the login screen upon successful registration.
 * </p>
 */
public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel viewModel;
    private EditText usernameEditText, emailEditText, locationEditText, passwordEditText, confirmPasswordEditText;

    /**
     * Initializes the activity, sets up UI components, and handles user interactions.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new RegisterViewModelFactory(this)).get(RegisterViewModel.class);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        locationEditText = findViewById(R.id.location);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        Button registerButton = findViewById(R.id.login_button);
        TextView signInTextView = findViewById(R.id.sign_up);

        // Observe LiveData
        viewModel.getRegisterSuccess().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        viewModel.getRegisterFailure().observe(this, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        );

        // Register button logic
        registerButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                User newUser = new User(username, email, SimpleCalendar.today(), password, location, "xChanger");
                viewModel.registerUser(newUser);
            }
        });

        // Navigate to Login Activity
        signInTextView.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
