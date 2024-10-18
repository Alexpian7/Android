package com.example.timeroster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    private boolean isDarkTheme;
    private SharedPreferences sharedPreferences;
    private TextView userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Manejo de insets para ajustar el padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar preferencias compartidas
        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean("darkTheme", false);

        // Configurar la vista
        userEmailTextView = findViewById(R.id.userEmail);
        String userEmail = sharedPreferences.getString("email", null);

        // Mostrar el correo del usuario logueado o un mensaje por defecto
        if (userEmail != null) {
            userEmailTextView.setText(userEmail);
        } else {
            userEmailTextView.setText("Time Roster");
        }

        // Configurar botones de navegación
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(view -> logout());
        findViewById(R.id.navHome).setOnClickListener(view -> navigateTo(HomeActivity.class));
        findViewById(R.id.navAddEmployee).setOnClickListener(view -> navigateTo(AddEmployeeActivity.class));
        findViewById(R.id.navEmployees).setOnClickListener(view -> navigateTo(EmployeesActivity.class));
        findViewById(R.id.navAttendance).setOnClickListener(view -> navigateTo(AttendanceActivity.class));
        findViewById(R.id.navAnalytics).setOnClickListener(view -> navigateTo(AnalyticsActivity.class));

        // Botón para cambiar el tema
        findViewById(R.id.themeSwitcher).setOnClickListener(view -> toggleTheme());
    }

    // Método para cerrar sesión
    private void logout() {
        // Limpiar las preferencias almacenadas
        sharedPreferences.edit().clear().apply();

        // Redirigir al Login
        Intent intent = new Intent(Dashboard.this, Login.class);
        startActivity(intent);
        finish(); // Cerrar esta actividad
    }

    // Cambiar entre tema claro y oscuro
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        sharedPreferences.edit().putBoolean("darkTheme", isDarkTheme).apply();
        // Aquí puedes agregar la lógica para aplicar el tema
    }

    // Método para la navegación entre actividades
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(Dashboard.this, targetActivity);
        startActivity(intent);
    }
}
