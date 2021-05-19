package com.example.firebasechatapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageModel> {

    public MessageAdapter(Context context, int resource, List<MessageModel> messages) {
        super(context, resource,messages);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        if(convertView==null){
            convertView=((Activity)getContext()).getLayoutInflater().inflate(R.layout.message_item,parent,false);
        }


        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        TextView textTextView  = convertView.findViewById(R.id.textTextView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);

        MessageModel message = getItem(position);

        boolean isText = message.getImageurl() == null;

        if (isText){
            textTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);

            textTextView.setText(message.getText());
        }
        else {
            textTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext()).load(message.imageurl).into(photoImageView);// библиотека glide
        }

        nameTextView.setText(message.getName());

        return convertView;
    }
}
