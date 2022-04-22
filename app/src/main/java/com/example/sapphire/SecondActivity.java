package com.example.sapphire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity  {

    ListView l_ItemCodeListView;
    MyAdapter adapter;
    String[] itemCode;
    String[] ItemName;
    String[] FileName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //SearchView initialization
        SearchView l_searchItemCode = (SearchView) findViewById(R.id.search);

        l_searchItemCode.setSubmitButtonEnabled(true);
        l_searchItemCode.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String s) {

                searchBtnClick(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
            });
        }



    public void searchBtnClick(String s)
    {
        ConnectionHelper l_ConnectionHelper = new ConnectionHelper();

             // Locate the EditText in listview_main.xml
            l_ConnectionHelper.ReFreshTable(s);

        l_ItemCodeListView = (ListView) findViewById(R.id.ItemCodeListView);
        itemCode = l_ConnectionHelper.getxItemCode();
        ItemName = l_ConnectionHelper.getxDescription();
        FileName = l_ConnectionHelper.getxFileName();

        adapter = new MyAdapter(this, itemCode, ItemName);

        // Binds the Adapter to the ListView
        l_ItemCodeListView.setAdapter(adapter);
        l_ItemCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent itemcodeIntent = new Intent(getBaseContext(), ItamCodeDetail.class);
                itemcodeIntent.putExtra( "ItemCode",itemCode[position].toString());
                itemcodeIntent.putExtra("ItemName",ItemName[position].toString());
                itemcodeIntent.putExtra("FileName",FileName[position].toString());
                startActivity(itemcodeIntent);
            }
        });
    }



    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[]/*, int imgs[]*/) {
            super(c, R.layout.list_view_items, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_view_items, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);

            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);
return row;
        }
    }
}










