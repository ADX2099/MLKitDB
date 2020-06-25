package com.huawei.mlkit.face.demo;

import android.provider.BaseColumns;

public final class FaceReaderContract {
    private FaceReaderContract(){ }

    public static class FaceEntry implements BaseColumns{
        public static final String TABLE_NAME          = "faces";
        public static final String age                 = "age";
        public static final String borderBottom        = "border_bottom";
        public static final String borderLeft          = "border_left";
        public static final String borderRight         = "border_right";
        public static final String xPointCoord         = "x_point_coord";
        public static final String yPointCoord         = "y_point_coord";
        public static final String faceHeight          = "face_height";
        public static final String faceWidth           = "face_width";
        public static final String mustache            = "mustache";
        public static final String sex                 = "sex";
        public static final String border_top          = "border_top";
    }
}
