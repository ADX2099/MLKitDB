package com.huawei.mlkit.face.demo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FaceModel implements  Serializable, Parcelable {
    public long id;
    public float borderBottom;
    public float borderLeft;
    public float borderRight;
    public float xPointCoord ;
    public float yPointCoord ;
    public float faceHeight;
    public float faceWidth ;
    public int age ;
    public float mustache ;
    public float sex ;
    public float borderTop;
   // public List<PositionObj> mlPositions;

    public FaceModel(){

    }

    protected FaceModel(Parcel in) {
        id = in.readInt();
        borderTop = in.readFloat();
        borderBottom = in.readFloat();
        borderLeft = in.readFloat();
        borderRight = in.readFloat();
        xPointCoord = in.readFloat();
        yPointCoord = in.readFloat();
        faceHeight = in.readFloat();
        faceWidth = in.readFloat();
        age = in.readInt();
        mustache = in.readFloat();
        sex = in.readFloat();
      // mlPositions = in.readArrayList(PositionObj.class.getClassLoader());

    }

    public static final Creator<FaceModel> CREATOR = new Creator<FaceModel>() {
        @Override
        public FaceModel createFromParcel(Parcel in) {
            return new FaceModel(in);
        }

        @Override
        public FaceModel[] newArray(int size) {
            return new FaceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeFloat(borderBottom);
        dest.writeFloat(borderLeft);
        dest.writeFloat(borderRight);
        dest.writeFloat(borderTop);
        dest.writeFloat(xPointCoord);
        dest.writeFloat(yPointCoord);
        dest.writeFloat(faceHeight);
        dest.writeFloat(faceWidth);
        dest.writeInt(age);
        dest.writeFloat(mustache);
        dest.writeFloat(sex);
       // mlPositions = new ArrayList<PositionObj>();

    }
}
