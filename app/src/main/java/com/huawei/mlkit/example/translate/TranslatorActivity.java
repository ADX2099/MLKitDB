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

package com.huawei.mlkit.example.translate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.common.MLException;
import com.huawei.hms.mlsdk.langdetect.MLDetectedLang;
import com.huawei.hms.mlsdk.langdetect.MLLangDetectorFactory;
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetector;
import com.huawei.hms.mlsdk.langdetect.cloud.MLRemoteLangDetectorSetting;
import com.huawei.hms.mlsdk.translate.MLTranslatorFactory;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslateSetting;
import com.huawei.hms.mlsdk.translate.cloud.MLRemoteTranslator;
import com.huawei.mlkit.example.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranslatorActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = TranslatorActivity.class.getSimpleName();

    private TextView mTextView;

    private EditText mEditText;

    private MLRemoteTranslator translator;

    private MLRemoteLangDetector langDetector;

    private static Map<String, String> nationAndCode = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_language_detection_translation);
        this.mTextView = this.findViewById(R.id.tv_output);
        this.mEditText = this.findViewById(R.id.et_input);
        this.findViewById(R.id.btn_translator).setOnClickListener(this);
        this.findViewById(R.id.btn_identification).setOnClickListener(this);
        TranslatorActivity.nationAndCode = this.readNationAndCode();
    }

    /**
     * Translation on the cloud. If you want to use cloud translator,
     * you need to apply for an agconnect-services.json file in the developer
     * alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc),
     * replacing the sample-agconnect-services.json in the project.
     */
    private void remoteTranslator() {
        // Create an analyzer. You can customize the analyzer by creating MLRemoteTranslateSetting
        MLRemoteTranslateSetting setting =
            new MLRemoteTranslateSetting.Factory().setTargetLangCode("zh").create();
        this.translator = MLTranslatorFactory.getInstance().getRemoteTranslator(setting);
        // Use default parameter settings.
        // analyzer = MLTranslatorFactory.getInstance().getRemoteTranslator();
        // Read text in edit box.
        String sourceText = this.mEditText.getText().toString();
        Task<String> task = this.translator.asyncTranslate(sourceText);
        task.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                // Recognition success.
                TranslatorActivity.this.remoteDisplaySuccess(text, true);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Recognition failure.
                TranslatorActivity.this.displayFailure(e);
            }
        });
    }

    /**
     * Language detection on the cloud. If you want to use cloud language detector,
     * you need to apply for an agconnect-services.json file in the developer
     * alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc),
     * replacing the sample-agconnect-services.json in the project.
     */
    private void remoteLangDetection() {
        // Create an analyzer. You can customize the analyzer by creating MLRemoteTextSetting
        MLRemoteLangDetectorSetting setting = new MLRemoteLangDetectorSetting.Factory().create();
        this.langDetector = MLLangDetectorFactory.getInstance().getRemoteLangDetector(setting);
        // Use default parameter settings.
        // analyzer = MLLangDetectorFactory.getInstance().getRemoteLangDetector();
        // Read text in edit box.
        String sourceText = this.mEditText.getText().toString();
        Task<List<MLDetectedLang>> task = this.langDetector.probabilityDetect(sourceText);
        task.addOnSuccessListener(new OnSuccessListener<List<MLDetectedLang>>() {
            @Override
            public void onSuccess(List<MLDetectedLang> text) {
                // Recognition success.
                TranslatorActivity.this.remoteDisplaySuccess(text);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Recognition failure.
                TranslatorActivity.this.displayFailure(e);
            }
        });
        // Returns the language code with the highest confidence, sourceText represents the language to be detected.
        /**
        Task<String> taskFirstBest = this.langDetector.firstBestDetect(sourceText);
        taskFirstBest.addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String text) {
                // Recognition success.
                TranslatorActivity.this.remoteDisplaySuccess(text, false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Recognition failure.
                TranslatorActivity.this.displayFailure(e);
            }
        });
         */
    }

    private void displayFailure(Exception exception) {
        String error = "Failure. ";
        try {
            MLException mlException = (MLException)exception;
            error += "error code: " + mlException.getErrCode() + "\n" + "error message: " + mlException.getMessage();
        } catch (Exception e) {
            error += e.getMessage();
        }
        this.mTextView.setText(error);
    }

    private void remoteDisplaySuccess(String text, boolean isTranslator) {
        if (isTranslator) {
            this.mTextView.setText(text);
        } else {
            this.mTextView.setText("Language=" + TranslatorActivity.nationAndCode.get(text) + "(" + text + ").");
        }
    }

    private void remoteDisplaySuccess(List<MLDetectedLang> result) {
        StringBuilder stringBuilder = new StringBuilder();
        for (MLDetectedLang recognizedLang : result) {
            String langCode = recognizedLang.getLangCode();
            float probability = recognizedLang.getProbability();
            stringBuilder.append("Language=" + TranslatorActivity.nationAndCode.get(langCode) + "(" + langCode + "), score=" + probability + ".\n");
        }
        this.mTextView.setText(stringBuilder.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.langDetector != null) {
            this.langDetector.stop();
        }
        if (this.translator != null) {
            this.translator.stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_translator:
                this.remoteTranslator();
                break;
            case R.id.btn_identification:
                this.remoteLangDetection();
                break;
            default:
                break;
        }
    }

    /**
     * Read the list of languages supported by language detection.
     * @return Returns a map that stores the country name and language code of the ISO 639-1.
     */
    private Map<String, String> readNationAndCode() {
        Map<String, String> nationMap = new HashMap<String, String>();
        InputStreamReader inputStreamReader = null;
        try {
            InputStream inputStream = this.getAssets().open("Country_pair_new.txt");
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (IOException e) {
            Log.d(TranslatorActivity.TAG, "Read Country_pair_new.txt failed.");
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] nationAndCodeList = line.split(" ");
                if (nationAndCodeList.length == 2) {
                    nationMap.put(nationAndCodeList[1], nationAndCodeList[0]);
                }
            }
        } catch (IOException e) {
            Log.d(TranslatorActivity.TAG, "Read Country_pair_new.txt line by line failed.");
        }
        return nationMap;
    }
}
