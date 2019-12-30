package com.example.getintouch.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getintouch.R;
import com.example.getintouch.adapter.Adapter;
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
import java.util.Iterator;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter chatsAdapter;
    private ArrayList<ModelClass> chatList;
   // private ArrayList<String> usersList;
    private ArrayList<Chat> usersList;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View chatsView = inflater.inflate(R.layout.fragment_chats, container, false);

       //getting the RecyclerView
        recyclerView = chatsView.findViewById(R.id.chats_recyclerView);
        usersList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(firebaseUser.getUid());
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    usersList.add(chat);
//                    if(chat.getSender().equals(firebaseUser.getUid())){
//                        usersList.add(chat.getReceiver());
//                    }
//                    if(chat.getReceiver().equals(firebaseUser.getUid())){
//                        usersList.add(chat.getSender());
//                    }
                }
                displayChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return chatsView;
    }

    private void displayChats() {

        chatList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ModelClass user = snapshot.getValue(ModelClass.class);

                    for (Chat chatlist : usersList){
                        if (user.getUserid().equals(chatlist.getReceiver())){
                            chatList.add(user);
                        }
                    }
//                    Iterator<String> iterator = usersList.iterator();
//                    while (iterator.hasNext()){
//                        String id = iterator.next();
//                        if(user.getUserid().equals(id)){
//                            if(chatList.size()!=0){
//                                Iterator<ModelClass> iterator2 = chatList.iterator();
//                                while (iterator2.hasNext()){
//                                    ModelClass user1 = iterator2.next();
//                                    if(!user.getUserid().equals(user1.getUserid())){
//                                        chatList.add(user);
//                                    }
//                                }
//                            }else {
//                                chatList.add(user);
//                            }
//                        }
//                    }

                }

                chatsAdapter = new Adapter(getContext(),chatList);
                recyclerView.setAdapter(chatsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
