package com.descolar.catchpokemon;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Clase RegisterActivity
 * Permite a los usuarios registrarse con un correo electrónico y una contraseña.
 * Tras el registro exitoso, el usuario es autenticado automáticamente.
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView backToLoginTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        backToLoginTextView = findViewById(R.id.backToLoginTextView);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Acción del botón de registro
        registerButton.setOnClickListener(v -> registerUser());

        // Volver a la pantalla de login
        backToLoginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /**
     * Registra un nuevo usuario con correo electrónico y contraseña.
     */
    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validar entradas
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(this, R.string.password_too_short, Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario en Firebase y autenticación automática
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, R.string.registration_successful, Toast.LENGTH_SHORT).show();
                        // Redirigir al usuario directamente a MainActivity
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, getString(R.string.registration_failed, task.getException().getMessage()),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
