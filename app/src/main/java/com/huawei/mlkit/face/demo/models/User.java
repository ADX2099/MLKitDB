package com.huawei.mlkit.face.demo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User  implements Serializable, Parcelable {
    public long id;
    public String user;
    public FaceModel faceModel;

    protected User(Parcel in) {
        id = in.readLong();
        user = in.readString();
        faceModel = in.readParcelable(FaceModel.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(user);
        dest.writeParcelable(faceModel, flags);
    }
}
