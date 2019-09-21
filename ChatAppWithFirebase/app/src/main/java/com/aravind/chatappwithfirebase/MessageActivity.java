package com.aravind.chatappwithfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aravind.chatappwithfirebase.Adapter.MessageAdapter;
import com.aravind.chatappwithfirebase.Adapter.VoiceMessageAdapter;
import com.aravind.chatappwithfirebase.Model.Chat;
import com.aravind.chatappwithfirebase.Model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    DatabaseReference reference;

    Intent intent;

    ImageButton btn_send;
    EditText text_send;

    MessageAdapter messageAdapter;
    VoiceMessageAdapter voiceMessageAdapter;
    List<Chat> mChat;
    RecyclerView recyclerView;
    String userid;
    StorageReference storageReference;

    private int STORAGE_PERMISSION_CODE = 1;
    //Audio record

    private ImageButton recordButton;
    MediaRecorder recorder = null;
    private static String fileName = null;//
    private static final String LOG_TAG = "record_Log";



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //Audio Record code

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String formatDate = df.format(c.getTime());

        recordButton = findViewById(R.id.record_btn_send);

       // Manually Setting up the Permissions to Our APP

        int permission_all =1;
        String[] Permissions = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermissions(this,Permissions)){
            ActivityCompat.requestPermissions(MessageActivity.this,Permissions,permission_all);
        }


//        ActivityCompat.requestPermissions(MessageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
//        ActivityCompat.requestPermissions(MessageActivity.this,new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName +="/"+formatDate+"recorded_audio.3gp";



        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    startRecording();
                    Toast.makeText(MessageActivity.this, "Recording voice..", Toast.LENGTH_SHORT).show();
                } if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    stopRecording();
                    Toast.makeText(MessageActivity.this, "Voice Recorded", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg  = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(),userid,msg);
                }else {
                    Toast.makeText(MessageActivity.this, "Please Type Message..", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
       // reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getSearch());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {

                    //change this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

                readMesages(fuser.getUid(),userid,user.getImageURL());
                readVoiceMessages(fuser.getUid(),userid,user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private  void sendMessage(String sender, String receiver, String msg){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",msg);
        reference.child("Chats").push().setValue(hashMap);

        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMesages(final String myid, final String userid, final String imageurl){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
       // reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startRecording() {
        try {

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recorder.prepare();
        recorder.start();


        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

    }

    private void stopRecording() {
        try{
            recorder.stop();
            uploadVoicMessage(fuser.getUid(),userid,fileName);
        }catch(RuntimeException stopException){
            //handle cleanup here
            recorder.release();
            recorder = null;
        }
    }

//
private void uploadVoicMessage(final String sender,final String receiver, final String voiceMsg){

        storageReference = FirebaseStorage.getInstance().getReference("AudioFiles");//


    Uri fileUri = Uri.fromFile(new File(voiceMsg));
   final StorageReference fileReference = storageReference.child(fileUri.getLastPathSegment());
   fileReference.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("sender",sender);
                    hashMap.put("receiver",receiver);
                    hashMap.put("voiceMessage",uri.toString());
                    reference.child("VoiceMessage").push().setValue(hashMap);

                    // add user to chat fragment
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                            .child(fuser.getUid())
                            .child(userid);

                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()){
                                chatRef.child("id").setValue(userid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MessageActivity.this, "uri not uploaded into database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MessageActivity.this, "Not Stored in Storage", Toast.LENGTH_SHORT).show();
        }
    });


}

    private void readVoiceMessages(final String myid, final String userid, final String imageurl) {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("VoiceMessage");
        // reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }

                    voiceMessageAdapter = new VoiceMessageAdapter(MessageActivity.this, mChat, imageurl);
                    recyclerView.setAdapter(voiceMessageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Manually Setup Permissions

    public static boolean hasPermissions(Context context,String... permissions){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for (String permission: permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!= PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

}
