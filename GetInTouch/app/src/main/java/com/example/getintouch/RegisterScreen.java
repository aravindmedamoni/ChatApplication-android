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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterScreen extends AppCompatActivity {

    //getting Firebase Database details
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    private TextInputEditText rg_userName,rg_userMail,rg_userPassword;
    private Button rg_registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        //this is toolbar code for enable backpress button and for set title
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register Screen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //finding the id's of the TextInputEditText and Buttons of the register screen xml

        rg_userName = findViewById(R.id.userName);
        rg_userMail = findViewById(R.id.userEmail);
        rg_userPassword = findViewById(R.id.userPassword);

        rg_registerButton = findViewById(R.id.registerButton);
        rg_registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = rg_userName.getText().toString();
                String userMail = rg_userMail.getText().toString();
                String userPassword = rg_userPassword.getText().toString();

                if(userMail.isEmpty() || userMail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(RegisterScreen.this, "All fields are Mandatory", Toast.LENGTH_SHORT).show();
                }else {
                    onRegister(userName,userMail,userPassword);
                }


            }
        });

        // First we need to get the Firebase Authentication Instance

        mAuth = FirebaseAuth.getInstance();

    }

    public void onRegister(final String userName, final String userEmail, String userPassword) {

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();

                            databaseReference = databaseReference.child(userId);

                            // Create HashMap To Pass the Details to the Firebase Database

                            HashMap<String,String> userDetails = new HashMap<>();
                            userDetails.put("userid", userId);
                            userDetails.put("username",userName);
                            userDetails.put("usermail",userEmail);

                            databaseReference.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        startActivity( new Intent(RegisterScreen.this,StartActivity.class));
                                        Toast.makeText(RegisterScreen.this, "Registration Success!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(RegisterScreen.this, "You can't Register with this mail and password", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
