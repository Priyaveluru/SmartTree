package cmpe235.smarttree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YuexingYin on 3/13/16.
 * This class provides all the database operations, including connecting database, all the transaction of database
 */


public class DBConnector extends SQLiteOpenHelper{
    //To store all the data in db we are using this class

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Smarttree.db";//define the name of database;

    //parameters for user table
    private static final String TABLE_USER = "user";//create a user table to store user data
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PHONE = "phone";

    //parameters for comment table
    private static final String TABLE_COMMENT= "comment";//create a comment table to store user's comment
    private static final String COLUMN_COMMENT = "comment";
    private static final String COLUMN_RANKING = "ranking";

    SQLiteDatabase db;


    public DBConnector(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create user table query
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EMAIL
                + " TEXT," + COLUMN_PASSWORD+ " TEXT," + COLUMN_USERNAME+" TEXT,"+ COLUMN_PHONE+" TEXT"+")";

        //create comment table query
        String CREATE_COMMENT_TABLE = "CREATE TABLE " +
                TABLE_COMMENT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_COMMENT
                + " TEXT," + COLUMN_RANKING + " TEXT," + COLUMN_USERNAME+" TEXT"+")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_COMMENT_TABLE);

        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS ";

        db.execSQL(query+TABLE_USER);
        db.execSQL(query+TABLE_COMMENT);
        this.onCreate(db);

    }

    //Sign up method
    public void insertUser(UserDetails user){

        db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, user.getEmail());
        contentValues.put(COLUMN_PASSWORD, user.getPassword());
        contentValues.put(COLUMN_USERNAME, user.getUsername());
        contentValues.put(COLUMN_PHONE, user.getPhone());

        db.insert(TABLE_USER, null, contentValues);

        db.close();


    }

    //user login method
    public String login(String email){

        db=this.getReadableDatabase();
        String query = "select password from user where email = '"+email+"'";

        Cursor cursor = db.rawQuery(query,null);

        cursor.moveToFirst();

        String password = cursor.getString(0);

        db.close();

        return password;
    }

    //insert user's comment in database
    public void insertComment(CommentDetails comment){

        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COMMENT, comment.getComment());
        contentValues.put(COLUMN_RANKING, comment.getRanking());
        contentValues.put(COLUMN_USERNAME, comment.getUsername());

        db.insert(TABLE_COMMENT, null, contentValues);
        db.close();
    }

    //get username from database
    public String getUsername(String password){

        db =this.getReadableDatabase();
        String query = "select username from user where password = '"+password+"'";

        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();

        String username = cursor.getString(0);

        db.close();

        return username;
    }
}
