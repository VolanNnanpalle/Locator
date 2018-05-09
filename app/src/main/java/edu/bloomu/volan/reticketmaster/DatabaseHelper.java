package edu.bloomu.volan.reticketmaster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This method creates the table in the database and inputs data into the database
 * Created by Volan on 11/21/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_information.db";
    private static final String TABLE_NAME = "new_account";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE_NUMBER_ = "phone_number";
    SQLiteDatabase database;

    public static final String TABLE_CREATE = "create table new_account (id integer primary key " +
            "not null, first_name text not null, last_name text not null, email " +
            "text not null, username text not null, password text not null, phone_number integer not null );";

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        //creates the table
        database.execSQL(TABLE_CREATE);
        this.database = database;
    }

    /**
     * This method inserts all the users information in database
     * @param userInformation
     */
    public void insertUserInformation(UserInformation userInformation)
    {
        database = this.getWritableDatabase();

        String query = "select * from new_account";
        Cursor cursor = database.rawQuery(query,null);
        int count = cursor.getCount();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FIRST_NAME,userInformation.getFirstName());
        contentValues.put(COLUMN_LAST_NAME,userInformation.getLastName());
        contentValues.put(COLUMN_EMAIL,userInformation.getEmail());
        contentValues.put(COLUMN_USERNAME,userInformation.getUsername());
        contentValues.put(COLUMN_PASSWORD,userInformation.getPassword());
        contentValues.put(COLUMN_PHONE_NUMBER_,userInformation.getPhoneNumber());
        contentValues.put(COLUMN_ID,count);

        database.insert(TABLE_NAME,null,contentValues);
        database.close();
    }

    /**
     * This method checks to see if the password is in the database
     * @param username
     * @return
     */
    public String searchPassword(String username)
    {
        database = this.getReadableDatabase();
        String query = "select username, password from "+TABLE_NAME;
        Cursor cursor = database.rawQuery(query,null);

        String tempUsername, tempPassword;
        tempPassword = "notFound";
        if(cursor.moveToFirst())
        {
            do {
                tempUsername = cursor.getString(0);

                if(tempUsername.equals(username)){
                    //store password
                    tempPassword = cursor.getString(1);
                    break;
                }


            }while(cursor.moveToNext());
        }
        return tempPassword;
    }

    /**
     * This method drops the table if it exist
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        //drops the table if it exists
        String query = "DROP TABLE IF EXISTS"+ TABLE_NAME;
        database.execSQL(query);
        this.onCreate(database);
    }

}
