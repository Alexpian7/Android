package com.example.timeroster;

import android.content.Intent; // Importa Intent para la redirección
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private boolean isNewUser = false;
    private EditText emailEditText, passwordEditText;
    private Button submitButton;
    private TextView switchTextView, errorTextView, successTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        submitButton = findViewById(R.id.button_submit);
        switchTextView = findViewById(R.id.user_start);
        errorTextView = findViewById(R.id.error);
        successTextView = findViewById(R.id.success);

        submitButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (isNewUser) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                successTextView.setVisibility(View.VISIBLE);
                                successTextView.setText("Usuario registrado con éxito");
                                errorTextView.setVisibility(View.GONE);
                            } else {
                                String errorMessage = getErrorMessage(task.getException());
                                errorTextView.setVisibility(View.VISIBLE);
                                errorTextView.setText(errorMessage);
                                successTextView.setVisibility(View.GONE);
                            }
                        });
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                successTextView.setVisibility(View.VISIBLE);
                                successTextView.setText("Inicio de sesión exitoso");
                                errorTextView.setVisibility(View.GONE);

                                // Redirigir a la actividad Dashboard
                                Intent intent = new Intent(Login.this, Dashboard.class);
                                startActivity(intent);
                                finish(); // Cierra la actividad de inicio de sesión
                            } else {
                                String errorMessage = getErrorMessage(task.getException());
                                errorTextView.setVisibility(View.VISIBLE);
                                errorTextView.setText(errorMessage);
                                successTextView.setVisibility(View.GONE);
                            }
                        });
            }
        });

        switchTextView.setOnClickListener(v -> {
            isNewUser = !isNewUser;
            if (isNewUser) {
                emailEditText.setVisibility(View.VISIBLE);
                submitButton.setText("Registrarse");
                switchTextView.setText("¿Ya tienes una cuenta? Iniciar sesión");
            } else {
                emailEditText.setVisibility(View.VISIBLE);
                submitButton.setText("Iniciar sesión");
                switchTextView.setText("¿No tienes una cuenta? Registrarse");
            }
        });
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthUserCollisionException) {
            return "El correo ya está en uso. Por favor, inicia sesión.";
        } else {
            return "Error: " + exception.getMessage();
        }
    }
}
