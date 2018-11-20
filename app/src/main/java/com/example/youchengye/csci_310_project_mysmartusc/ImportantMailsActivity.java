package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImportantMailsActivity extends AppCompatActivity {
    private static List<Header> importantEmails = new ArrayList<>();
    private static ListView listView;
    public List<Header> getImportantEmails() {
        return importantEmails;
    }

    public static void addHeaders(List<Header> headers){
        for (Header h: headers){
            importantEmails.add(0, h);
        }
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_important_mails);

        listView = (ListView) findViewById(R.id.importantMailsListView);

        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return importantEmails.size();
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
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.list_item,null);

            TextView from = (TextView) view.findViewById(R.id.textView_From);
            TextView subject = (TextView) view.findViewById(R.id.textView_Subject);
            TextView snippet = (TextView) view.findViewById(R.id.textView_Snippet);

            from.setText(importantEmails.get(position).from);
            subject.setText(importantEmails.get(position).subject);
            snippet.setText(importantEmails.get(position).snippet);
            return view;
        }
    }


}
