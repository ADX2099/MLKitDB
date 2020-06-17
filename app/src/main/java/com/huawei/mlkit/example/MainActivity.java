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

package com.huawei.mlkit.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.mlkit.example.asr.AsrAnalyseActivity;
import com.huawei.mlkit.example.bankCard.BcrAnalyseActivity;
import com.huawei.mlkit.example.classification.ImageClassificationAnalyseActivity;
import com.huawei.mlkit.example.document.ImageDocumentAnalyseActivity;
import com.huawei.mlkit.example.face.LiveFaceAnalyseActivity;
import com.huawei.mlkit.example.face.StillFaceAnalyseActivity;
import com.huawei.mlkit.example.generalCard.GcrAnalyseActivity;
import com.huawei.mlkit.example.IDCard.IcrAnalyseActivity;
import com.huawei.mlkit.example.imgseg.ImageSegmentationLiveAnalyseActivity;
import com.huawei.mlkit.example.imgseg.ImageSegmentationStillAnalyseActivity;
import com.huawei.mlkit.example.landmark.ImageLandmarkAnalyseActivity;
import com.huawei.mlkit.example.object.LiveObjectAnalyseActivity;
import com.huawei.mlkit.example.productvisionsearch.ProductVisionSearchAnalyseActivity;
import com.huawei.mlkit.example.text.ImageTextAnalyseActivity;
import com.huawei.mlkit.example.translate.TranslatorActivity;
import com.huawei.mlkit.example.tts.TtsAnalyseActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        this.findViewById(R.id.btn_face_live).setOnClickListener(this);
        this.findViewById(R.id.btn_face_image).setOnClickListener(this);
        this.findViewById(R.id.btn_text).setOnClickListener(this);
        this.findViewById(R.id.btn_object).setOnClickListener(this);
        this.findViewById(R.id.btn_document).setOnClickListener(this);
        this.findViewById(R.id.btn_classification).setOnClickListener(this);
        this.findViewById(R.id.btn_landmark).setOnClickListener(this);
        this.findViewById(R.id.btn_translate).setOnClickListener(this);
        this.findViewById(R.id.btn_productvisionsearch).setOnClickListener(this);
        this.findViewById(R.id.btn_imgseg_image).setOnClickListener(this);
        this.findViewById(R.id.btn_imgseg_live).setOnClickListener(this);
        this.findViewById(R.id.btn_icr).setOnClickListener(this);
        this.findViewById(R.id.btn_bcr).setOnClickListener(this);
        this.findViewById(R.id.btn_gcr).setOnClickListener(this);
        this.findViewById(R.id.btn_tts).setOnClickListener(this);
        this.findViewById(R.id.btn_asr).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_face_live:
                this.startActivity(new Intent(MainActivity.this, LiveFaceAnalyseActivity.class));
                break;
            case R.id.btn_face_image:
                this.startActivity(new Intent(MainActivity.this, StillFaceAnalyseActivity.class));
                break;
            case R.id.btn_classification:
                this.startActivity(new Intent(MainActivity.this, ImageClassificationAnalyseActivity.class));
                break;
            case R.id.btn_object:
                this.startActivity(new Intent(MainActivity.this, LiveObjectAnalyseActivity.class));
                break;
            case R.id.btn_document:
                this.startActivity(new Intent(MainActivity.this, ImageDocumentAnalyseActivity.class));
                break;
            case R.id.btn_landmark:
                this.startActivity(new Intent(MainActivity.this, ImageLandmarkAnalyseActivity.class));
                break;
            case R.id.btn_text:
                this.startActivity(new Intent(MainActivity.this, ImageTextAnalyseActivity.class));
                break;
            case R.id.btn_translate:
                this.startActivity(new Intent(MainActivity.this, TranslatorActivity.class));
                break;
            case R.id.btn_productvisionsearch:
                this.startActivity(new Intent(MainActivity.this, ProductVisionSearchAnalyseActivity.class));
                break;
            case R.id.btn_imgseg_live:
                this.startActivity(new Intent(MainActivity.this, ImageSegmentationLiveAnalyseActivity.class));
                break;
            case R.id.btn_imgseg_image:
                this.startActivity(new Intent(MainActivity.this, ImageSegmentationStillAnalyseActivity.class));
                break;
            case R.id.btn_icr:
                this.startActivity(new Intent(MainActivity.this, IcrAnalyseActivity.class));
                break;
            case R.id.btn_bcr:
                this.startActivity(new Intent(MainActivity.this, BcrAnalyseActivity.class));
                break;
            case R.id.btn_gcr:
                this.startActivity(new Intent(MainActivity.this, GcrAnalyseActivity.class));
                break;
            case R.id.btn_tts:
                this.startActivity(new Intent(MainActivity.this, TtsAnalyseActivity.class));
                break;
            case R.id.btn_asr:
                this.startActivity(new Intent(MainActivity.this, AsrAnalyseActivity.class));
                break;
            default:
                break;
        }
    }
}
