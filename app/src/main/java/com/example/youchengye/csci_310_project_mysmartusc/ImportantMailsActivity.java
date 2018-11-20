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
    private List<Header> importantEmails = new ArrayList<>();
    private ListView listView;
    public List<Header> getImportantEmails() {
        return importantEmails;
    }

    public void addHeaders(List<Header> headers){
        importantEmails.addAll(headers);
        ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        importantEmails.add(new Header("Ruoxi Jia","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia2","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia3","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia4","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia5","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia6","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia7","Testing","listview","asdf","words"));
//        importantEmails.add(new Header("Ruoxi Jia8","Testing","listview","asdf","words"));
        importantEmails.addAll(LoginActivity.saveEmails);
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
