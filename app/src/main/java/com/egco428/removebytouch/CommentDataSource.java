package com.egco428.removebytouch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 6272user on 10/20/2016 AD.
 */
public class CommentDataSource {
    // link SQLite with Object
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allCoumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_COMMENT};

    public CommentDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Comment createComment(String comment){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT,comment);
        long insertID = database.insert(MySQLiteHelper.TABLE_COMMENTS,null,values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,allCoumns,MySQLiteHelper.COLUMN_ID+" = "+insertID,null,null,null,null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();

        return newComment;

    }

    // delete first item
    public void deleteComment(Comment comment){
        long id = comment.getId();
        System.out.println("Comment deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS,MySQLiteHelper.COLUMN_ID+" = "+id,null); // (comment,_id = id,null)
    }

    // open program and load all comment
    public List<Comment> getAllComments(){
        List<Comment> comments = new ArrayList<Comment>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,allCoumns,null,null,null,null,null);
        cursor.moveToFirst();
        // get record and save in comment
        while(!cursor.isAfterLast()){
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }

        cursor.close();
        return comments;

    }

    public Comment cursorToComment(Cursor cursor){
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0)); // 0 = first column
        comment.setComment(cursor.getString(1));
        return comment;
    }


}
