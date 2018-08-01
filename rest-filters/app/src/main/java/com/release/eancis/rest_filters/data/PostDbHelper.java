package com.release.eancis.rest_filters.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.release.eancis.rest_filters.data.PostContract.*;
import com.release.eancis.rest_filters.model.PostItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Holystar on 11/06/18.
 */

public class PostDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "post.db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_BUSINESSCARD_TABLE = "CREATE TABLE " + PostsEntry.TABLE_NAME + " (" +
                PostsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PostsEntry.COLUMN_POST_REMOTE_ID + " INTEGER, " +
                PostsEntry.COLUMN_POST_USER_ID + " INTEGER, " +
                PostsEntry.COLUMN_POST_TITLE + " TEXT, " +
                PostsEntry.COLUMN_POST_DESCRIPTION + " TEXT, " +
                PostsEntry.COLUMN_POST_PUBLISHED_AT + " TEXT, " +
                PostsEntry.COLUMN_POST_IMAGE + " TEXT " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_BUSINESSCARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //This method might be modified to ALTER the table in the right way
        //This is an Example
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PostsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     *
     * Methos on DB
     *
     * */

    //Read all posts
    public  List<PostItem> loadAllPosts(){
        Cursor posts;
        SQLiteDatabase db = this.getReadableDatabase();

        posts = db.query(
                PostsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return returnPostItemList(posts);
    }

    //Read filterd posts
    public  List<PostItem> loadFilteredPosts(Integer _filter){
        Cursor posts;
        SQLiteDatabase db = this.getReadableDatabase();

        posts = db.query(
                PostsEntry.TABLE_NAME,
                null,
                PostsEntry.COLUMN_POST_USER_ID +  "=?",
                new String[] {String.valueOf(_filter)},
                null,
                null,
                PostsEntry.COLUMN_POST_REMOTE_ID);

        return returnPostItemList(posts);
    }

    //It lest to return to the MainActivity (the VIEW) the List of PostItem directly
    private List<PostItem> returnPostItemList(Cursor posts){
        List<PostItem> postList = new ArrayList<PostItem>();

        if(posts.getCount() > 0){
            while(posts.moveToNext()){
                PostItem  post = new PostItem(
                    posts.getLong(posts.getColumnIndex(PostsEntry.COLUMN_POST_REMOTE_ID)),
                    posts.getLong(posts.getColumnIndex(PostsEntry.COLUMN_POST_USER_ID)),
                    posts.getString(posts.getColumnIndex(PostsEntry.COLUMN_POST_TITLE)),
                    posts.getString(posts.getColumnIndex(PostsEntry.COLUMN_POST_DESCRIPTION)),
                    posts.getString(posts.getColumnIndex(PostsEntry.COLUMN_POST_PUBLISHED_AT)),
                    posts.getString(posts.getColumnIndex(PostsEntry.COLUMN_POST_IMAGE))
                );
                postList.add(post);
            }
            posts.close();
        }

        return  postList;
    }

    //Insert a post
    public long insertPost(PostItem _post){
        long newID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PostsEntry.COLUMN_POST_REMOTE_ID, _post.getId().toString());
        cv.put(PostsEntry.COLUMN_POST_USER_ID, _post.getUserId().toString());
        cv.put(PostsEntry.COLUMN_POST_TITLE, _post.getTitle());
        cv.put(PostsEntry.COLUMN_POST_DESCRIPTION, _post.getDescription());
        cv.put(PostsEntry.COLUMN_POST_PUBLISHED_AT, _post.getPublishedAt());
        cv.put(PostsEntry.COLUMN_POST_IMAGE, _post.getImage());

        newID = db.insert(PostsEntry.TABLE_NAME, null, cv);

        return newID;
    }

    //delete all posts
    public boolean deletePosts() {
        boolean result;
        SQLiteDatabase db = this.getWritableDatabase();

        result = db.delete(
                PostsEntry.TABLE_NAME,
                null,
                null) > 0;

        return result;
    }

    // CLOSE DB
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if ( db != null && db.isOpen() ) {
            db.close();
        }
    }

}
