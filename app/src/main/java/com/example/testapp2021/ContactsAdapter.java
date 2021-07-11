package com.example.testapp2021;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contact> {
    private Context context;
    //Creating a list of objects(contact)
    private List<Contact> contacts;

    public ContactsAdapter(Context context, List<Contact> list) {
        super(context,R.layout.row_layout,list);
        this.context = context;
        this.contacts = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.row_layout,parent,false);

        TextView tvChar = convertView.findViewById(R.id.tvChar);
        TextView tvName = convertView.findViewById(R.id.tvContactName);
        TextView tvMail = convertView.findViewById(R.id.tvEmail);

        tvChar.setText(contacts.get(position).getName().toUpperCase().charAt(0) +"");
        tvName.setText(contacts.get(position).getName());
        tvMail.setText(contacts.get(position).getEmail());


        return convertView;
    }
}
