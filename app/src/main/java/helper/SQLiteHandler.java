package helper;

/**
 * Created by A10 on 16/01/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAMA = "nama";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ALAMAT_EMAIL = "alamat_email";
    private static final String KEY_ALAMAT_PENGIRIMAN = "alamat_pengiriman";
    private static final String KEY_NO_TELP = "no_telp";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_USER_ID = "user_id";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAMA + " TEXT,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_ALAMAT_EMAIL + " TEXT,"
                + KEY_ALAMAT_PENGIRIMAN + " TEXT,"
                + KEY_NO_TELP + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_USER_ID + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String nama,
                        String username,
                        String nama_perusahaan,
                        String alamat_perusahaan,
                        String no_telp,
                        String uid,
                        String created_at,
                        String user_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAMA, nama);
        values.put(KEY_USERNAME, username);
        values.put(KEY_ALAMAT_EMAIL, nama_perusahaan);
        values.put(KEY_ALAMAT_PENGIRIMAN, alamat_perusahaan);
        values.put(KEY_NO_TELP, no_telp);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);
        values.put(KEY_USER_ID, user_id);

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("nama", cursor.getString(1));
            user.put("username", cursor.getString(2));
            user.put("alamat_email", cursor.getString(3));
            user.put("alamat_pengiriman", cursor.getString(4));
            user.put("no_telp", cursor.getString(5));
            user.put("uid", cursor.getString(6));
            user.put("created_at", cursor.getString(7));
            user.put("user_id", cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
