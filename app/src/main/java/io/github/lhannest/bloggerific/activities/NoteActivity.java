package io.github.lhannest.bloggerific.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;

import io.github.lhannest.bloggerific.MyTagHandler;
import io.github.lhannest.bloggerific.R;
import io.github.lhannest.bloggerific.data.DatabaseHelper;
import io.github.lhannest.bloggerific.data.Note;

public class NoteActivity extends AppCompatActivity {
    EditText noteContent;
    EditText noteTitle;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteContent = (EditText) findViewById(R.id.noteContent);
        noteTitle = (EditText) findViewById(R.id.noteTitle);
        databaseHelper = new DatabaseHelper(this);

        long noteId = getIntent().getExtras().getLong("note_id", -1);

        if (noteId != -1) {
            Note note = databaseHelper.getNote(noteId, false);

            noteTitle.setText(note.title);
            noteContent.setText(android.text.Html.fromHtml(note.content));
        }

    }
}
