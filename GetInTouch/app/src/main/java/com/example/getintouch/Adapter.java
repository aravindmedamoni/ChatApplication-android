package com.example.getintouch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getintouch.model.ModelClass;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter {

    private ArrayList<ModelClass> modelClassArrayList;
    private Context context;

    public Adapter(Context context,ArrayList<ModelClass> modelClassArrayList) {
        this.modelClassArrayList = modelClassArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info_layout,parent,false);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ModelClass user = modelClassArrayList.get(position);
        String username = user.getUsername();
        ((UserAdapterViewHolder)holder).setData(username);


        // below code is to add the onclick Actions to the userList of the Users Screen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MessageActivity.class);
                intent.putExtra("userid",user.getUserid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    // This viewHolder for showing the userList to the main screen of the User tab
    public static class UserAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        public UserAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userName);
        }

        private void setData(String userName){
            username.setText(userName);
        }
    }

}
