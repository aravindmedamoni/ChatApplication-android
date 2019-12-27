package com.example.getintouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

  // Getting the Firebase Details
    FirebaseAuth mAuth;

  //Declare the TextInputEditText and Button variable of the xml

  private TextInputEditText lgn_usermail, lgn_userPassowrd;
  private Button lgn_signIButton,lgn_RegisterButton;

    @Override
    protected void onStart() {
        super.onStart();

        // Check weather any user already login or not if login direct goto the mainScreen
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }else {
            Toast.makeText(this, "Login Here", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Setting the backPress Arrow and title to the toolBar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login Screen");

        //finding the TextInputEditText and Button with their id's

        lgn_usermail = findViewById(R.id.userEmail);
        lgn_userPassowrd = findViewById(R.id.userPassword);

        lgn_signIButton = findViewById(R.id.loginButton);
        lgn_RegisterButton = findViewById(R.id.registerButton);


        //Getting the FirebaseAuthentication instance
        mAuth = FirebaseAuth.getInstance();


        // Now we need to give the Action to the Login Button

        lgn_signIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userMail = lgn_usermail.getText().toString();
                String userPassword = lgn_userPassowrd.getText().toString();
                if(userMail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(StartActivity.this, "all fields required to login", Toast.LENGTH_SHORT).show();
                }else {
                    onLogin(userMail,userPassword);
                }
            }
        });

    }

    private void onLogin(String userMail, String userPassword) {

        mAuth.signInWithEmailAndPassword(userMail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity( new Intent(StartActivity.this,MainActivity.class));
                    Toast.makeText(StartActivity.this, "Login SuccessFully", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(StartActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRegister(View view) {
        startActivity(new Intent(this,RegisterScreen.class));
    }
}
