package com.aravind.chatappwithfirebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aravind.chatappwithfirebase.MessageActivity;
import com.aravind.chatappwithfirebase.Model.Chat;
import com.aravind.chatappwithfirebase.Model.User;
import com.aravind.chatappwithfirebase.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

private Context mContext;
private List<Chat> mChat;
private String imageurl;
public static final int MSG_TYPE_LEFT =0;
public static final int MSG_TYPE_RIGHT =1;

FirebaseUser fUser;

public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;

        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    if (viewType == MSG_TYPE_RIGHT){
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
        return new MessageAdapter.ViewHolder(view);
    }else {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
        return new MessageAdapter.ViewHolder(view);
    }

}

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    Chat chat = mChat.get(position);

    holder.show_message.setText(chat.getMessage());

    if (imageurl.equals("default")){
        holder.profile_imageview.setImageResource(R.mipmap.ic_launcher);
    } else {
        Glide.with(mContext).load(imageurl).into(holder.profile_imageview);
    }
        }

@Override
public int getItemCount() {
        return mChat.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder{

    public TextView show_message;
    public ImageView profile_imageview;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        show_message = itemView.findViewById(R.id.show_message);
        profile_imageview = itemView.findViewById(R.id.profile_image);
    }
}

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
