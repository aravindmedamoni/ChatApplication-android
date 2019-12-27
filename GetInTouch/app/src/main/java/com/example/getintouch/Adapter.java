package com.example.getintouch;

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

    public Adapter(ArrayList<ModelClass> modelClassArrayList) {
        this.modelClassArrayList = modelClassArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_info_layout,parent,false);
        return new UserAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String username = modelClassArrayList.get(position).getUsername();
        ((UserAdapterViewHolder)holder).setData(username);

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
