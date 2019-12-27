package com.example.getintouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getintouch.model.ModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private TextView userName;
    private EditText textMessage;
    private ImageButton sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Getting the toolbar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //getting the CircleImageView and TextView from xml
        profileImage = findViewById(R.id.userProfileImage);
        userName = findViewById(R.id.userName);
        textMessage = findViewById(R.id.message);
        sendButton = findViewById(R.id.btn_send);

        // getting the getIntent to get the info passed through this intent
        Intent intent = getIntent();
      final String userid = intent.getStringExtra("userid");

        // get the current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = textMessage.getText().toString();
                if(!message.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,message);
                }else {
                    Toast.makeText(MessageActivity.this, "you can't send empty message!", Toast.LENGTH_SHORT).show();
                }

                textMessage.setText("");

            }
        });


        // getting the details of the user from the firebase

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelClass user = dataSnapshot.getValue(ModelClass.class);
                userName.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMessage(String sender, String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,String> messageDetails = new HashMap<>();
        messageDetails.put("sender",sender);
        messageDetails.put("receiver",receiver);
        messageDetails.put("message",message);

        databaseReference.child("Chats").push().setValue(messageDetails);
    }
}
