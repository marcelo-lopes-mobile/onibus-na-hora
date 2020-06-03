package br.com.onibusnahora.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.onibusnahora.R;
import br.com.onibusnahora.ui.app.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    // View Components
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    // Database Components
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setListener();
        setLoginAction();
        setIntentForRegister();
    }

    private void setListener(){
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Toast.makeText(this, "Faça o login!", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        };
    }

    private void setLoginAction() {
        btnLogin = findViewById(R.id.login_btnLogin);
        btnLogin.setOnClickListener(v -> {
            etEmail = findViewById(R.id.login_etEmail);
            etPassword = findViewById(R.id.login_etPassword);
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();

            verifyAndLogin();
        });
    }

    private void verifyAndLogin() {
        if (email.isEmpty()){
            etEmail.setError("Informe seu email!");
            etEmail.requestFocus();
        } else if(password.isEmpty()){
            etPassword.setError("Insira sua senha!");
            etPassword.requestFocus();
        } else{
            loginWithEmailAndPassword();
        }
    }

    private void loginWithEmailAndPassword() {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
            if (task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            } else {
                Toast.makeText(this, "Não foi possível efetuar o login... Tente novamente!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void setIntentForRegister(){
        tvRegister = findViewById(R.id.login_tvRegister);
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
}
