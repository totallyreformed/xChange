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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        Spinner roleSpinner = findViewById(R.id.roleSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.login_roles, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        roleSpinner.setAdapter(adapter);

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);
        TextView signUpTextView = findViewById(R.id.sign_up);

        // Observe LiveData
        viewModel.getLoginSuccess().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "Login Successful: " + user.getUsername(), Toast.LENGTH_SHORT).show();

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

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                String selectedRole = roleSpinner.getSelectedItem().toString();
                if (selectedRole.equals("Admin")) {
                    viewModel.loginAsAdmin(username, password);
                } else {
                    viewModel.loginAsXChanger(username, password);
                }
            }
        });
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent); // Redirect to RegisterActivity
        });

    }
}
