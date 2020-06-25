package com.huawei.mlkit.face.demo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.huawei.mlkit.face.demo.utils.FaceConstants;

public class DBHelper extends SQLiteOpenHelper {
    private Context ctx;
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + "Faces";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = FaceConstants.DBNAME;

    final String create  = "CREATE TABLE " + FaceReaderContract.FaceEntry.TABLE_NAME +
            " (" + FaceReaderContract.FaceEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FaceReaderContract.FaceEntry.age +" INTEGER, " +
            FaceReaderContract.FaceEntry.borderBottom + " REAL," +
            FaceReaderContract.FaceEntry.borderLeft + " REAL, " +
            FaceReaderContract.FaceEntry.borderRight + " REAL, " +
            FaceReaderContract.FaceEntry.xPointCoord + " REAL, " +
            FaceReaderContract.FaceEntry.yPointCoord + " REAL, " +
            FaceReaderContract.FaceEntry.faceHeight + " REAL, " +
            FaceReaderContract.FaceEntry.faceWidth + " REAL, " +
            FaceReaderContract.FaceEntry.mustache + " REAL, " +
            FaceReaderContract.FaceEntry.sex + " REAL, " +
            FaceReaderContract.FaceEntry.border_top + " REAL)";
    final String createUser = "CREATE TABLE " + FaceReaderContract.UserEntry.TABLE_NAME +
            " (" + FaceReaderContract.UserEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FaceReaderContract.UserEntry.userName + " TEXT," +
            FaceReaderContract.UserEntry.userAge + " INTEGER," +
            FaceReaderContract.UserEntry.userFaceHeight + " REAL, " +
            FaceReaderContract.UserEntry.userFaceWidth + " REAL, " +
            FaceReaderContract.UserEntry.userMustache + " REAL, " +
            FaceReaderContract.UserEntry.userSex + " REAL)";
    String query = create + createUser;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
