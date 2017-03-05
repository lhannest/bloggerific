package io.github.lhannest.bloggerific.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.lhannest.bloggerific.HttpRequester;
import io.github.lhannest.bloggerific.R;

public class BloggerImportActivity extends AppCompatActivity {
    EditText searchEditText;
    Button searchBtn;
    ListView postListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogger_import);

        searchEditText = (EditText) findViewById(R.id.searchBloggerTxt);
        searchBtn = (Button) findViewById(R.id.searchBloggerBtn);
        postListView = (ListView) findViewById(R.id.searchBloggerListview);

        Intent oauth2Intent = new Intent(this, OAuth2Activity.class);
        startActivityForResult(oauth2Intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            final String oauth2token = data.getExtras().getString(OAuth2Activity.INTENT_KEY);

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (searchEditText.getText().toString().isEmpty()) return;

                    String queryTerms = searchEditText.getText().toString();

                    // Removed for git commit
                    String blogId;

                    searchForPosts(blogId, queryTerms, oauth2token);


                }
            });

        }
    }

    private void searchForPosts(String blogId, String queryTerms, String oauth2token) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    final JSONArray jsonArray = json.getJSONArray("items");

                    String[] postNames = new String[jsonArray.length()];

                    for (int i = 0; i < postNames.length; i++) {
                        JSONObject jsonPost = jsonArray.getJSONObject(i);
                        postNames[i] = jsonPost.getString("title");
                    }

                    postListView.setAdapter(new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            postNames
                    ));

                    postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject jsonPost = jsonArray.getJSONObject(position);

                                String title = jsonPost.getString("title");
                                String content = jsonPost.getString("content");

                                importPost(title, content);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        queryTerms = queryTerms.replace(" ", "+");

        String url = "https://www.googleapis.com/blogger/v3/blogs/"
                + blogId + "/posts/search?q=" + queryTerms + "&access_token=" + oauth2token;

        HttpRequester.makeRequest(BloggerImportActivity.this, Request.Method.GET, url, responseListener);


    }

    private void importPost(String title, String content) {
        String wordCount = Integer.toString(content.split(" ").length);

        new AlertDialog.Builder(BloggerImportActivity.this)
                .setTitle("Import this post?")
                .setMessage("Title: " + title + "\nWord Count: " + wordCount)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .create().show();
    }
}
