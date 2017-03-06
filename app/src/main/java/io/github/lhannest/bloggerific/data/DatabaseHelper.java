package io.github.lhannest.bloggerific.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 05/03/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "NoteDataBase.db";
    public static final int version = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE notes (_id INTEGER PRIMARY KEY, title TEXT, body TEXT, dateCreated LONG, dateLastModified LONG)";

    public DatabaseHelper(Context context) {
        super(context, databaseName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
//        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public int deleteNote(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("notes", "_id = ?",  new String[] { Long.toString(id) });
    }

    public long createNote(String title, String body) {
        long time = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("body", body);
        values.put("dateCreated", time);
        values.put("dateLastModified", time);

        long id = getWritableDatabase().insert("notes", null, values);

        return id;
    }

    public Note getNote(long id, boolean withholdContent) {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery;
        if (withholdContent) {
            selectQuery = " SELECT _id, title, dateCreated, dateLastModified "
                        + " FROM notes "
                        + " WHERE _id=" +Long.toString(id)
                        + " ORDER BY dateLastModified DESC;";
        } else {
            selectQuery = " SELECT _id, title, body, dateCreated, dateLastModified"
                        + " FROM notes "
                        + " WHERE _id=" +Long.toString(id)
                        + " ORDER BY dateLastModified DESC;";
        }

        Cursor cursor = db.rawQuery(selectQuery, null);


        Note note = null;
        if (cursor != null && cursor.moveToFirst()) {
            Long noteId = cursor.getLong(cursor.getColumnIndex("_id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            Long dateCreated = cursor.getLong(cursor.getColumnIndex("dateCreated"));
            Long dateLastModified = cursor.getLong(cursor.getColumnIndex("dateLastModified"));

            if (withholdContent) {
                note = new Note(noteId, title, null, dateCreated, dateLastModified);
            } else {
                String body = cursor.getString(cursor.getColumnIndex("body"));
                note = new Note(noteId, title, body, dateCreated, dateLastModified);
            }
        }

        return note;
    }

    public void updateNote(long id, String title, String body) {
        SQLiteDatabase db = getWritableDatabase();

        long time = System.currentTimeMillis();

        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("body", body);
        values.put("dateLastModified", time);

        getWritableDatabase().update("notes", values, "_id = " + Long.toString(id), null);
    }

    /**
     *
     * @param withholdContent
     * Set this to true to avoid loading the content of posts. The content isn't always
     * needed, and loading it would be wasteful.
     * @return
     */
    public Note[] getAllNotes(boolean withholdContent) {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery;
        if (withholdContent) {
            selectQuery = "SELECT _id, title, dateCreated, dateLastModified "
                        + "FROM notes ORDER BY dateLastModified DESC";
        } else {
            selectQuery = "SELECT _id, title, body, dateCreated, dateLastModified FROM notes ORDER BY dateLastModified DESC";
        }

        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Note> notes = new ArrayList<Note>();

        if (cursor.moveToFirst()) {
            do {
                Long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                Long dateCreated = cursor.getLong(cursor.getColumnIndex("dateCreated"));
                Long dateLastModified = cursor.getLong(cursor.getColumnIndex("dateLastModified"));

                Note note;
                if (withholdContent) {
                    note = new Note(id, title, null, dateCreated, dateLastModified);
                } else {
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    note = new Note(id, title, body, dateCreated, dateLastModified);
                }

                notes.add(note);

            } while (cursor.moveToNext());
        }

        return notes.toArray(new Note[notes.size()]);
    }

}
