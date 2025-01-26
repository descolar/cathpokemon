package com.descolar.catchpokemon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Actividad de inicio de sesión para la aplicación.
 * Permite a los usuarios iniciar sesión con correo/contraseña o con Google.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001; // Código para el inicio de sesión con Google

    // Componentes de la interfaz
    private EditText emailEditText, passwordEditText;
    private Button loginButton, googleLoginButton;
    private TextView registerTextView;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    // Cliente de inicio de sesión con Google
    private GoogleSignInClient mGoogleSignInClient;

    /**
     * Método llamado cuando se crea la actividad.
     * Inicializa la interfaz y las configuraciones de Firebase y Google Sign-In.
     *
     * @param savedInstanceState Estado guardado de la actividad (opcional).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos las vistas de la interfaz
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        googleLoginButton = findViewById(R.id.googleLoginButton);
        registerTextView = findViewById(R.id.registerTextView);

        // Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configuramos Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("718251124292-js1eb7k5va5lfig3kulddllsei4725qj.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configuramos el botón de inicio de sesión con Google
        googleLoginButton.setOnClickListener(v -> signInWithGoogle());

        // Configuramos el botón de inicio de sesión con correo y contraseña
        loginButton.setOnClickListener(v -> signInWithEmail());

        // Configuramos el enlace para redirigir al registro
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Inicia el flujo de inicio de sesión con Google.
     */
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Inicia sesión con correo y contraseña.
     * Muestra un mensaje en caso de error.
     */
    private void signInWithEmail() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Autentica al usuario con Firebase usando las credenciales de Google.
     *
     * @param account Cuenta de Google obtenida tras el inicio de sesión.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("LoginActivity", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                        Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    /**
     * Maneja los resultados del inicio de sesión con Google.
     *
     * @param requestCode Código de solicitud.
     * @param resultCode  Código de resultado.
     * @param data        Datos del intento.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(Exception.class);
                firebaseAuthWithGoogle(account);
            } catch (Exception e) {
                Log.w("LoginActivity", "Google sign-in failed", e);
                Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Actualiza la interfaz del usuario después de iniciar sesión.
     *
     * @param user Usuario autenticado o null si el inicio de sesión falló.
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
