package com.bulbulproject.bulbul;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Followers extends AppCompatActivity {

    ListView followerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        followerList = (ListView) findViewById(R.id.followers_list);

        followerList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_follower, parent, false);

                TextView name = (TextView) convertView.findViewById(R.id.name);
                name.setText("Name" + position + " Surname" + position);
                return convertView;
            }
        });
    }
}
