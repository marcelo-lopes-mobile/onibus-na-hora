package br.com.onibusnahora.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.com.onibusnahora.R;
import br.com.onibusnahora.ui.app.HomeActivity;

public class RegisterActivity extends AppCompatActivity {

    // View Components
    EditText etEmail, etPassword, etConfirmPassword;
    Button btnRegister;
    TextView tvLogin;

    // Data Components
    String email, password, confirmPassword;

    // Database Components
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setRegisterAction();
        setLoginRedirect();
    }


    private void setRegisterAction() {
        btnRegister = findViewById(R.id.register_btnRegister);
        btnRegister.setOnClickListener(v -> {
            getValuesEmailPasswordAndConfirmPassword();
            verifyAndRegister();
        });
    }

    private void verifyAndRegister() {
        if (email.isEmpty()){
            etEmail.setError("Insira seu email!");
            etEmail.requestFocus();
        } else if (password.isEmpty() | password.length() < 6) {
            etPassword.setError("Crie uma senha com mais de 6 dígitos");
            etPassword.requestFocus();
        } else if (confirmPassword.isEmpty() | confirmPassword.length() < 6 ) {
            etConfirmPassword.setError("Confirme sua senha!");
            etConfirmPassword.requestFocus();
        } else if (!password.equals(confirmPassword)){
            etConfirmPassword.setError("As senhas não conferem!");
            etConfirmPassword.requestFocus();
        } else {
            registerAndRedirect();
        }
    }

    private void registerAndRedirect() {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, task -> {
            if (task.isSuccessful()){
                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                Toast.makeText(this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Falha ao cadastrar! Tente novamente...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getValuesEmailPasswordAndConfirmPassword() {
        etEmail = findViewById(R.id.register_etEmail);
        etPassword = findViewById(R.id.register_etPassword);
        etConfirmPassword = findViewById(R.id.register_etConfirmPassword);
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();
    }

    private void setLoginRedirect() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }
}
