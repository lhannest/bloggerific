package io.github.lhannest.bloggerific.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import io.github.lhannest.bloggerific.R;

public class BloggerImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogger_import);

        EditText searchEditText = (EditText) findViewById(R.id.searchBloggerTxt);
        Button searchBtn = (Button) findViewById(R.id.searchBloggerBtn);
        ListView postListView = (ListView) findViewById(R.id.searchBloggerListview);

        Intent oauth2Intent = new Intent(this, OAuth2Activity.class);
        startActivityForResult(oauth2Intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String oauth2token = data.getExtras().getString(OAuth2Activity.INTENT_KEY);
        }
    }
}
