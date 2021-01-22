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

package com.huawei.mlkit.example.imgseg;

import java.io.IOException;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.LensEngine;
import com.huawei.hms.mlsdk.common.MLAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentation;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationAnalyzer;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationScene;
import com.huawei.hms.mlsdk.imgseg.MLImageSegmentationSetting;
import com.huawei.mlkit.example.R;
import com.huawei.mlkit.example.camera.GraphicOverlay;
import com.huawei.mlkit.example.camera.LensEnginePreview;

public class ImageSegmentationLiveAnalyseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ImageSegmentationLiveAnalyseActivity.class.getSimpleName();

    private static final int CAMERA_PERMISSION_CODE = 1;

    private MLImageSegmentationAnalyzer analyzer;

    private LensEngine mLensEngine;

    private LensEnginePreview mPreview;

    private GraphicOverlay mOverlay;

    private int lensType = LensEngine.FRONT_LENS;

    private boolean isFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_live_segment_analyse);
        this.mPreview = this.findViewById(R.id.segment_preview);
        this.mOverlay = this.findViewById(R.id.segment_overlay);
        this.findViewById(R.id.facingSwitch).setOnClickListener(this);
        if (savedInstanceState != null) {
            this.lensType = savedInstanceState.getInt("lensType");
        }
        this.createSegmentAnalyzer();
        // Checking Camera Permissions.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            this.createLensEngine();
        } else {
            this.requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[] {Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, CAMERA_PERMISSION_CODE);
            return;
        }
    }

    private void createLensEngine() {
        Context context = this.getApplicationContext();
        // Create LensEngine.
        this.mLensEngine = new LensEngine.Creator(context, this.analyzer).setLensType(this.lensType)
                .applyDisplayDimension(960, 720)
                .applyFps(25.0f)
                .enableAutomaticFocus(true)
                .create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != CAMERA_PERMISSION_CODE) {
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

    @Override
    protected void onResume() {
        super.onResume();
        this.startLensEngine();
    }

    @Override
    public void onClick(View v) {
        this.isFront = !this.isFront;
        if (this.isFront) {
            this.lensType = LensEngine.FRONT_LENS;
        } else {
            this.lensType = LensEngine.BACK_LENS;
        }
        if (this.mLensEngine != null) {
            this.mLensEngine.close();
        }
        this.createLensEngine();
        this.startLensEngine();
    }

    private void createSegmentAnalyzer() {
        // Create a segmentation analyzer. You can create an analyzer using the provided customized face detection parameter: MLImageSegmentationSetting
        MLImageSegmentationSetting setting = new MLImageSegmentationSetting.Factory()
                .setExact(false)
                .setScene(MLImageSegmentationScene.FOREGROUND_ONLY)
                .setAnalyzerType(MLImageSegmentationSetting.BODY_SEG)
                .create();
        this.analyzer = MLAnalyzerFactory.getInstance().getImageSegmentationAnalyzer(setting);
        this.analyzer.setTransactor(new MLAnalyzer.MLTransactor<MLImageSegmentation>() {
            @Override
            public void destroy() {

            }

            @Override
            public void transactResult(MLAnalyzer.Result<MLImageSegmentation> result) {
                mOverlay.clear();
                SparseArray<MLImageSegmentation> imageSegmentationResult = result.getAnalyseList();
                MLSegmentGraphic graphic = new MLSegmentGraphic(mPreview, mOverlay, imageSegmentationResult.valueAt(0), isFront);
                mOverlay.add(graphic);
            }
        });
    }

    private void startLensEngine() {
        if (this.mLensEngine != null) {
            try {
                this.mPreview.start(this.mLensEngine, this.mOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Failed to start lens engine.", e);
                this.mLensEngine.release();
                this.mLensEngine = null;
            }
        }
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
                Log.e(TAG, "Stop failed: " + e.getMessage());
            }
        }
    }
}
