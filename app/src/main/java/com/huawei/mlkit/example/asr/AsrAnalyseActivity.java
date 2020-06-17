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

package com.huawei.mlkit.example.asr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hms.mlplugin.asr.MLAsrCaptureActivity;
import com.huawei.hms.mlplugin.asr.MLAsrCaptureConstants;
import com.huawei.mlkit.example.R;

public class AsrAnalyseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = AsrAnalyseActivity.class.getSimpleName();
    private static final int HANDLE_CODE = 0;
    private static final String HANDLE_KEY = "text";
    private static final int AUDIO_PERMISSION_CODE = 1;
    private static final int ML_ASR_CAPTURE_CODE = 2;
    private TextView mTextView;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case HANDLE_CODE:
                    String text = message.getData().getString(HANDLE_KEY);
                    mTextView.setText(text + "\n");
                    Log.e(TAG, text);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_voice_asr);
        this.mTextView = this.findViewById(R.id.textView);
        findViewById(R.id.voice_input).setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            this.requestCameraPermission();
        }
    }

    private void requestCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            ActivityCompat.requestPermissions(this, permissions, AsrAnalyseActivity.AUDIO_PERMISSION_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != AsrAnalyseActivity.AUDIO_PERMISSION_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
    }

    private void displayResult(String str) {
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString(HANDLE_KEY, str);
        msg.setData(data);
        msg.what = HANDLE_CODE;
        handler.sendMessage(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice_input:
                // If you want to use ASR, you need to apply for an agconnect-services.json file in the developer
                // alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc),
                // replacing the sample-agconnect-services.json in the project.
                // Use Intent for recognition settings.
                Intent intent = new Intent(this, MLAsrCaptureActivity.class)
                        // Set the language that can be recognized to English. If this parameter is not set,
                        // English is recognized by default. Example: "zh": Chinese or "en-US": English
                        .putExtra(MLAsrCaptureConstants.LANGUAGE, "en-US")
                        // Set whether to display text on the speech pickup UI. MLAsrCaptureConstants.FEATURE_ALLINONE: no;
                        // MLAsrCaptureConstants.FEATURE_WORDFLUX: yes.
                        .putExtra(MLAsrCaptureConstants.FEATURE, MLAsrCaptureConstants.FEATURE_WORDFLUX);
                // ML_ASR_CAPTURE_CODE: request code between the current activity and speech pickup UI activity.
                // You can use this code to obtain the processing result of the speech pickup UI.
                startActivityForResult(intent, ML_ASR_CAPTURE_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String text = "";
        if (null == data) {
            displayResult("Intent data is null.");
        }
        // ML_ASR_CAPTURE_CODE: request code between the current activity and speech pickup UI activity.
        if (requestCode == ML_ASR_CAPTURE_CODE) {
            switch (resultCode) {
                // MLAsrCaptureConstants.ASR_SUCCESS: Recognition is successful.
                case MLAsrCaptureConstants.ASR_SUCCESS:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        // Obtain the text information recognized from speech.
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_RESULT)) {
                            text = bundle.getString(MLAsrCaptureConstants.ASR_RESULT);
                        }
                        if (text == null || "".equals(text)) {
                            text = "Result is null.";
                        }
                        // Process the recognized text information.
                        displayResult(text);
                    }
                    break;
                // MLAsrCaptureConstants.ASR_FAILURE: Recognition fails.
                case MLAsrCaptureConstants.ASR_FAILURE:
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        // Check whether a result code is contained.
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_CODE)) {
                            text = text + bundle.getInt(MLAsrCaptureConstants.ASR_ERROR_CODE);
                        }
                        // Check whether error information is contained.
                        if (bundle != null && bundle.containsKey(MLAsrCaptureConstants.ASR_ERROR_MESSAGE)) {
                            String errorMsg = bundle.getString(MLAsrCaptureConstants.ASR_ERROR_MESSAGE);
                            if (errorMsg != null && !"".equals(errorMsg)) {
                                text = "[" + text + "]" + errorMsg;
                            }
                        }
                    }
                    displayResult(text);
                default:
                    displayResult("Failure.");
                    break;
            }
        }
    }
}
