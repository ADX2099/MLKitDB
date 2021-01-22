package com.huawei.mlkit.face.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huawei.mlkit.face.demo.db.DBHelper;
import com.huawei.mlkit.face.demo.db.FaceReaderContract;
import com.huawei.mlkit.face.demo.models.FaceModel;
import com.huawei.mlkit.face.demo.utils.FaceConstants;

import java.util.ArrayList;

public class SavingActivity extends AppCompatActivity {
    FaceModel faceModel;
    Button buttonInfo;
    ArrayList<FaceModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);
        Bundle extras = getIntent().getExtras();
        if(extras != null){

            faceModel = extras.getParcelable(FaceConstants.FACE);
            saveIntoDB(faceModel);

        }

        Intent theIntent = new Intent(Intent.ACTION_VIEW);
        //Define tu protocolo
        theIntent.setData(Uri.parse("pushscheme://com.huawei.codelabpush/deeplink?"));
        //Agrega los parametros a tu intent
        theIntent.putExtra("nombre","Soro");
        theIntent.putExtra("edad","27");
        theIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String intentUri = theIntent.toUri(Intent.URI_INTENT_SCHEME);

        Intent gotIntent = getIntent();
        if(null != gotIntent){
            //Obtenemos la informacion del intent
            String nombre = gotIntent.getData().getQueryParameter("nombre");
            int edad = Integer.parseInt(gotIntent.getData().getQueryParameter("edad"));
        }

    }

    private void initViews() {
        buttonInfo = findViewById(R.id.buttonInfo);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList = getFaces();
                for (FaceModel face:arrayList) {

                }
            }
        });
    }

    private void saveIntoDB(FaceModel faceModel) {
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FaceReaderContract.FaceEntry.age, faceModel.age);
        values.put(FaceReaderContract.FaceEntry.borderBottom, faceModel.borderBottom);
        values.put(FaceReaderContract.FaceEntry.borderLeft, faceModel.borderLeft);
        values.put(FaceReaderContract.FaceEntry.borderRight, faceModel.borderRight);
        values.put(FaceReaderContract.FaceEntry.xPointCoord, faceModel.xPointCoord);
        values.put(FaceReaderContract.FaceEntry.yPointCoord, faceModel.yPointCoord);
        values.put(FaceReaderContract.FaceEntry.faceHeight, faceModel.faceHeight);
        values.put(FaceReaderContract.FaceEntry.faceWidth, faceModel.faceWidth);
        values.put(FaceReaderContract.FaceEntry.mustache, faceModel.mustache);
        values.put(FaceReaderContract.FaceEntry.sex, faceModel.sex);
        values.put(FaceReaderContract.FaceEntry.border_top, faceModel.borderTop);
        long newRowId = db.insert(FaceConstants.FACES, null, values);


    }

    public ArrayList<FaceModel> getFaces(){
        DBHelper dbHelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String[] projection = new String[]{
                BaseColumns._ID,
                FaceReaderContract.FaceEntry.age,
                FaceReaderContract.FaceEntry.borderBottom,
                FaceReaderContract.FaceEntry.borderLeft,
                FaceReaderContract.FaceEntry.borderRight,
                FaceReaderContract.FaceEntry.xPointCoord,
                FaceReaderContract.FaceEntry.yPointCoord,
                FaceReaderContract.FaceEntry.faceHeight,
                FaceReaderContract.FaceEntry.faceWidth,
                FaceReaderContract.FaceEntry.mustache,
                FaceReaderContract.FaceEntry.sex,
                FaceReaderContract.FaceEntry.border_top,
        };

        Cursor cursor = db.query(FaceConstants.FACES, projection, null, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
        ArrayList<FaceModel> array = new ArrayList<>();
        while (!cursor.isAfterLast()) {

            FaceModel faceModel = new FaceModel();
            faceModel.id = cursor.getColumnIndexOrThrow(FaceReaderContract.FaceEntry._ID);
            faceModel.age = cursor.getInt(1);
            faceModel.borderBottom = cursor.getFloat(2);
            faceModel.borderLeft = cursor.getFloat(3);
            faceModel.borderRight = cursor.getFloat(4);
            faceModel.xPointCoord = cursor.getFloat(5);
            faceModel.yPointCoord = cursor.getFloat(6);
            faceModel.faceHeight = cursor.getFloat(7);
            faceModel.faceWidth = cursor.getFloat(8);
            faceModel.mustache = cursor.getFloat(9);
            faceModel.sex = cursor.getFloat(10);
            faceModel.borderTop = cursor.getFloat(11);
            array.add(faceModel);
            cursor.moveToNext();
        }
        cursor.close();
        return array;
    }


}