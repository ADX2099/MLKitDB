package com.huawei.mlkit.face.demo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PositionObj implements Serializable, Parcelable {
    float x;
    float y;
    float z;

    public PositionObj(){

    }

    protected PositionObj(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
    }

    public static final Creator<PositionObj> CREATOR = new Creator<PositionObj>() {
        @Override
        public PositionObj createFromParcel(Parcel in) {
            return new PositionObj(in);
        }

        @Override
        public PositionObj[] newArray(int size) {
            return new PositionObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeFloat(z);
    }
}
