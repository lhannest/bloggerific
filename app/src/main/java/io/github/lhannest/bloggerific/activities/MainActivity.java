package io.github.lhannest.bloggerific.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import io.github.lhannest.bloggerific.R;
import io.github.lhannest.bloggerific.data.DatabaseHelper;
import io.github.lhannest.bloggerific.data.Note;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listView = (ListView) findViewById(R.id.mainListView);
        databaseHelper = new DatabaseHelper(this);

        refreshListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
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
        } else if (id == R.id.action_import_from_blogger) {
            Intent intent = new Intent(this, BloggerImportActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshListView() {
        final Note[] notes = databaseHelper.getAllNotes(true);
        String[] titles = new String[notes.length];

        for (int i = 0; i < notes.length; i++) {
            titles[i] = notes[i].title;
        }


        listView.setAdapter(new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                titles
        ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long noteId = notes[position].localId;
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("note_id", noteId);
                startActivity(intent);
            }
        });
    }
}
