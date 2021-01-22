/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.mlkit.example.object;

import java.io.IOException;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.objects.MLObject;
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzer;
import com.huawei.hms.mlsdk.objects.MLObjectAnalyzerSetting;
import com.huawei.mlkit.example.R;
import com.huawei.mlkit.example.camera.GraphicOverlay;
import com.huawei.mlkit.example.camera.LensEnginePreview;

public class LiveObjectAnalyseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LiveObjectAnalyseActivity.class.getSimpleName();

    private static final int CAMERA_PERMISSION_CODE = 1;

    private MLObjectAnalyzer analyzer;

    private LensEngine mLensEngine;

    private boolean isStarted = true;

    private LensEnginePreview mPreview;

    private GraphicOverlay mOverlay;

    private int lensType = LensEngine.BACK_LENS;

    public boolean mlsNeedToDetect = true;

    private final static int STOP_PREVIEW = 1;

    private final static int START_PREVIEW = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_live_object_analyse);
        if (savedInstanceState != null) {
            this.lensType = savedInstanceState.getInt("lensType");
        }
        this.mPreview = this.findViewById(R.id.object_preview);
        this.mOverlay = this.findViewById(R.id.object_overlay);
        this.createObjectAnalyzer();
        Button start = this.findViewById(R.id.detect_start);
        start.setOnClickListener(this);
        // Checking Camera Permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            this.createLensEngine();
        } else {
            this.requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, LiveObjectAnalyseActivity.CAMERA_PERMISSION_CODE);
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.startLensEngine();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mPreview.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mLensEngine != null) {
            this.mLensEngine.release();
        }
        if (this.analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
                Log.e(LiveObjectAnalyseActivity.TAG, "Stop failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LiveObjectAnalyseActivity.CAMERA_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.createLensEngine();
            return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("lensType", this.lensType);
        super.onSaveInstanceState(savedInstanceState);
    }

    // When you need to implement a scene that stops after recognizing specific content
    // and continues to recognize after finishing processing, refer to this code
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LiveObjectAnalyseActivity.START_PREVIEW:
                    LiveObjectAnalyseActivity.this.mlsNeedToDetect = true;
                    Log.d("object", "start to preview");
                    LiveObjectAnalyseActivity.this.startPreview();
                    break;
                case LiveObjectAnalyseActivity.STOP_PREVIEW:
                    LiveObjectAnalyseActivity.this.mlsNeedToDetect = false;
                    Log.d("object", "stop to preview");
                    LiveObjectAnalyseActivity.this.stopPreview();
                    break;
                default:
                    break;
            }
        }
    };

    private void stopPreview() {
        this.mlsNeedToDetect = false;
        if (this.mLensEngine != null) {
            this.mLensEngine.release();
        }
        if (this.analyzer != null) {
            try {
                this.analyzer.stop();
            } catch (IOException e) {
                Log.d("object", "Stop failed: " + e.getMessage());
            }
        }
        this.isStarted = false;
    }

    private void startPreview() {
        if (this.isStarted) {
            return;
        }
        this.createObjectAnalyzer();
        this.mPreview.release();
        this.createLensEngine();
        this.startLensEngine();
        this.isStarted = true;
    }

    @Override
    public void onClick(View v) {
        this.mHandler.sendEmptyMessage(LiveObjectAnalyseActivity.START_PREVIEW);
    }

    private void createObjectAnalyzer() {
        // Create an object analyzer
        // Use MLObjectAnalyzerSetting.TYPE_VIDEO for video stream detection.
        // Use MLObjectAnalyzerSetting.TYPE_PICTURE for static image detection.
        MLObjectAnalyzerSetting setting =
                new MLObjectAnalyzerSetting.Factory().setAnalyzerType(MLObjectAnalyzerSetting.TYPE_VIDEO)
                        .allowMultiResults()
                        .allowClassification()
                        .create();
        this.analyzer = MLAnalyzerFactory.getInstance().getLocalObjectAnalyzer(setting);
        this.analyzer.setTransactor(new MLAnalyzer.MLTransactor<MLObject>() {
            @Override
            public void destroy() {

            }

            @Override
            public void transactResult(MLAnalyzer.Result<MLObject> result) {
                if (!LiveObjectAnalyseActivity.this.mlsNeedToDetect) {
                    return;
                }
                LiveObjectAnalyseActivity.this.mOverlay.clear();
                SparseArray<MLObject> objectSparseArray = result.getAnalyseList();
                for (int i = 0; i < objectSparseArray.size(); i++) {
                    MLObjectGraphic graphic = new MLObjectGraphic(LiveObjectAnalyseActivity.this.mOverlay, objectSparseArray.valueAt(i));
                    LiveObjectAnalyseActivity.this.mOverlay.add(graphic);
                }
                // When you need to implement a scene that stops after recognizing specific content
                // and continues to recognize after finishing processing, refer to this code
                for (int i = 0; i < objectSparseArray.size(); i++) {
                    if (objectSparseArray.valueAt(i).getTypeIdentity() == MLObject.TYPE_FOOD) {
                        LiveObjectAnalyseActivity.this.mlsNeedToDetect = true;
                        LiveObjectAnalyseActivity.this.mHandler.sendEmptyMessage(LiveObjectAnalyseActivity.STOP_PREVIEW);
                    }
                }
            }
        });
    }

    private void createLensEngine() {
        Context context = this.getApplicationContext();
        // Create LensEngine
        this.mLensEngine = new LensEngine.Creator(context, this.analyzer).setLensType(this.lensType)
                .applyDisplayDimension(640, 480)
                .applyFps(25.0f)
                .enableAutomaticFocus(true)
                .create();
    }

    private void startLensEngine() {
        if (this.mLensEngine != null) {
            try {
                this.mPreview.start(this.mLensEngine, this.mOverlay);
            } catch (IOException e) {
                Log.e(LiveObjectAnalyseActivity.TAG, "Failed to start lens engine.", e);
                this.mLensEngine.release();
                this.mLensEngine = null;
            }
        }
    }
}
