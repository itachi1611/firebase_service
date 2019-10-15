package com.fox.firebase;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnRegister, btnRecover;
    private ImageView imgFacebook;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private String TAG = "Firebase Auth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnRecover.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
            finish();
        }
    }

    private void initViews(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnRecover = findViewById(R.id.btnRecover);
        progressBar = findViewById(R.id.progressBar);
        imgFacebook = findViewById(R.id.imgFacebook);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                onLogin();
                break;
            case R.id.btnRegister:
                onRegister();
                break;
            case R.id.btnRecover:
                onRecover();
                break;
            case R.id.imgFacebook:
                onFacebookAuthentication();
                break;
        }
    }

    private void onRecover() {
        startActivity(new Intent(MainActivity.this, RecoverActivity.class));
    }

    private void onValidateData(String email, String password){
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Email can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this, "Password can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length() < 5){
            Toast.makeText(MainActivity.this, "Password at least have 5 character", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void onRegister() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        onValidateData(email, password);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "createUserWithEmail:success", task.getException());
                        Toast.makeText(MainActivity.this, "Registration succeeded.", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }

                    // ...
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void onLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        onValidateData(email,password);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(MainActivity.this, "Sign in succeeded.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Sign in  failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    // ...
                }
            });
    }

    public void onFacebookAuthentication() {
        startActivity(new Intent(MainActivity.this, FacebookActivity.class));
    }
}
