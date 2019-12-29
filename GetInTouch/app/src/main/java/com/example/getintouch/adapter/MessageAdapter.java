package com.example.getintouch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getintouch.R;
import com.example.getintouch.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int RIGHT_SIDE_MESSAGE =0;
    private static final int LEFT_SIDE_MESSAGE=1;


    private ArrayList<Chat> chatArrayList;
    private Context context;
    private String imageurl;

    public MessageAdapter(Context context, ArrayList<Chat> chatArrayList, String imageurl) {
        this.chatArrayList = chatArrayList;
        this.context = context;
        this.imageurl = imageurl;
    }


    @Override
    public int getItemViewType(int position) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatArrayList.get(position).getSender().equals(firebaseUser.getUid())){
            return RIGHT_SIDE_MESSAGE;
        }else {
            return LEFT_SIDE_MESSAGE;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case RIGHT_SIDE_MESSAGE:
                View rightView = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_chat_message,parent,false);
                return new MessageViewHolder(rightView);
            case LEFT_SIDE_MESSAGE:
                View leftView = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_chat_message,parent,false);
                return new MessageViewHolder(leftView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chatMessage = chatArrayList.get(position);
        ((MessageViewHolder) holder).show_message.setText(chatMessage.getMessage());

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profileImage;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profileImage = itemView.findViewById(R.id.profileImage);
        }

//        private void setMessage(String message,String imageurl){
//            show_message.setText(message);
//        }
    }
}
