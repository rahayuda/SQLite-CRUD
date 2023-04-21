package com.example.biodata;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity { String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    @SuppressLint("StaticFieldLeak")
    public static MainActivity ma;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
        public void onClick(View arg0) {
            Intent inte = new Intent(MainActivity.this, BuatBiodata.class);
            startActivity(inte);
        }
        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();
    }
    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase(); cursor = db.rawQuery("SELECT * FROM biodata", null); daftar = new String[cursor.getCount()]; cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.listView);
        ListView01.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, daftar));
        ListView01.setSelected(true);

        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() { @Override
        public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3)
        {
            final String selection = daftar[arg2];
            final CharSequence[] dialogitem = {"View", "Update",
                    "Delete"};
            AlertDialog.Builder builder = new
                    AlertDialog.Builder(MainActivity.this);
            //builder.setTitle(" ");
            builder.setItems(dialogitem, new DialogInterface.OnClickListener() { @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent i = new Intent(getApplicationContext(), LihatBiodata.class);
                        i.	putExtra("nama", selection);
                        startActivity(i);
                        break;
                    case 1:
                        Intent in = new Intent(getApplicationContext(), UpdateBiodata.class);
                        in.putExtra("nama", selection);
                        startActivity(in);
                        break;
                    case 2:
                        SQLiteDatabase db = dbcenter.getWritableDatabase(); db.execSQL("delete from biodata where nama = '" + selection + "'");
                        RefreshList();
                        break;
                         }
                     }
                });
                builder.create().show();
            }});
            ((ArrayAdapter<?>)ListView01.getAdapter()).notifyDataSetInvalidated();
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
