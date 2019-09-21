package com.aravind.chatappwithfirebase.Adapter;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aravind.chatappwithfirebase.Model.Chat;
import com.aravind.chatappwithfirebase.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

public class VoiceMessageAdapter extends RecyclerView.Adapter<VoiceMessageAdapter.ViewHolder> {

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT =1;
    MediaPlayer mediaPlayer;

    FirebaseUser fUser;

    public VoiceMessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;

    }

    @NonNull
    @Override
    public VoiceMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.voice_chat_item_right,parent,false);
            return new VoiceMessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.voice_chat_item_left,parent,false);
            return new VoiceMessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull VoiceMessageAdapter.ViewHolder holder, int position) {

       final Chat chat = mChat.get(position);

        holder.play_Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String voice = chat.getVoiceMessage();

               mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(voice);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

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

        public ImageButton play_Voice;
        public ImageView profile_imageview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            play_Voice = itemView.findViewById(R.id.play_Button);
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

