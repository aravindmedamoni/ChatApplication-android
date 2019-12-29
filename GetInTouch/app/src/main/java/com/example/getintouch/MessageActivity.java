package com.example.getintouch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.getintouch.adapter.MessageAdapter;
import com.example.getintouch.model.Chat;
import com.example.getintouch.model.ModelClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    //below CircleImageView and TextView are belongs to toolbar
    private CircleImageView profileImage;
    private TextView userName;

    // Below two widgets are belongs send messages Relative layout
    private EditText textMessage;
    private ImageButton sendButton;

    //getting the RecyclerView to show the messages
    private RecyclerView recyclerView;
    private RecyclerView.Adapter messageAdapter;
    private ArrayList<Chat> chatArrayList;

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

        // getting the getIntent to get the info which is passed through the intent
        Intent intent = getIntent();
      final String userid = intent.getStringExtra("userid");

        // get the current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = textMessage.getText().toString().trim();
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
                readMessages(firebaseUser.getUid(),userid,user.getImageurl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //message recyclerView info

        recyclerView = findViewById(R.id.messages_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        //create ArrayList for the messages

    }

    //Below method is used to send the data to the database and
    // it store the data by using sendid and receiverid
    public void sendMessage(String sender, String receiver, String message){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,String> messageDetails = new HashMap<>();
        messageDetails.put("sender",sender);
        messageDetails.put("receiver",receiver);
        messageDetails.put("message",message);

        //push() is used to create a uniqueId to the each message send by the sender
        databaseReference.child("Chats").push().setValue(messageDetails);
    }

    public void readMessages(final String myId, final String userId, final String imageUrl){

        chatArrayList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chatMessage = snapshot.getValue(Chat.class);
                    if(chatMessage.getReceiver().equals(myId) && chatMessage.getSender().equals(userId) || chatMessage.getReceiver().equals(userId) && chatMessage.getSender().equals(myId)){
                        chatArrayList.add(chatMessage);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,chatArrayList,imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onVoiceRecord(View view) {
        Toast.makeText(this, "Clicked Record Button", Toast.LENGTH_SHORT).show();
    }
}
