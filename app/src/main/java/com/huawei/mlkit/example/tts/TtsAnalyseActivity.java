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

package com.huawei.mlkit.example.tts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.mlsdk.tts.MLTtsCallback;
import com.huawei.hms.mlsdk.tts.MLTtsConfig;
import com.huawei.hms.mlsdk.tts.MLTtsConstants;
import com.huawei.hms.mlsdk.tts.MLTtsEngine;
import com.huawei.hms.mlsdk.tts.MLTtsError;
import com.huawei.hms.mlsdk.tts.MLTtsWarn;
import com.huawei.mlkit.example.R;

public class TtsAnalyseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = TtsAnalyseActivity.class.getSimpleName();
    private static final int HANDLE_CODE = 1;
    private static final String HANDLE_KEY = "text";
    private EditText mEditText;
    private TextView mTextView;

    MLTtsEngine mlTtsEngine;
    MLTtsConfig mlConfigs;

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
        this.setContentView(R.layout.activity_voice_tts);
        this.findViewById(R.id.btn_speak).setOnClickListener(this);
        this.findViewById(R.id.btn_stop_speak).setOnClickListener(this);
        this.mEditText = this.findViewById(R.id.edit_input);
        this.mTextView = this.findViewById(R.id.textView);
        // Method 1: Use the default parameter settings to create a TTS engine.
        // In the default settings, the source language is Chinese, the Chinese female voice is used,
        // the voice speed is 1.0 (1x), and the volume is 1.0 (1x).
        // MLTtsConfig mlConfigs = new MLTtsConfig();
        // Method 2: Use customized parameter settings to create a TTS engine.
        mlConfigs = new MLTtsConfig()
                // Set the text converted from speech to English.
                // MLTtsConstants.TTS_EN_US: converts text to English.
                // MLTtsConstants.TTS_ZH_HANS: converts text to Chinese.
                .setLanguage(MLTtsConstants.TTS_EN_US)
                // Set the English timbre.
                // MLTtsConstants.TTS_SPEAKER_FEMALE_ZH: Chinese female voice.
                // MLTtsConstants.TTS_SPEAKER_MALE_ZH: Chinese male voice.
                .setPerson(MLTtsConstants.TTS_SPEAKER_FEMALE_EN)
                // Set the speech speed. Range: 0.2–1.8. 1.0 indicates 1x speed.
                .setSpeed(1.0f)
                // Set the volume. Range: 0.2–1.8. 1.0 indicates 1x volume.
                .setVolume(1.0f);
        mlTtsEngine = new MLTtsEngine(mlConfigs);
        // Pass the TTS callback to the TTS engine.
        mlTtsEngine.setTtsCallback(callback);
    }

    /**
     * TTS callback function.. If you want to use TTS,
     * you need to apply for an agconnect-services.json file in the developer
     * alliance(https://developer.huawei.com/consumer/en/doc/development/HMS-Guides/ml-add-agc),
     * replacing the sample-agconnect-services.json in the project.
     */
    MLTtsCallback callback = new MLTtsCallback() {
        @Override
        public void onError(String taskId, MLTtsError err) {
            // Processing logic for TTS failure.
            String str = "TaskID: " + taskId + ", error:" + err;
            displayResult(str);
        }

        @Override
        public void onWarn(String taskId, MLTtsWarn warn) {
            // Alarm handling without affecting service logic.
            String str = "TaskID: " + taskId + ", warn:" + warn;
            displayResult(str);
        }

        @Override
        public void onRangeStart(String taskId, int start, int end) {
            // Process the mapping between the currently played segment and text.
            String str = "TaskID: " + taskId + ", onRangeStart [" + start + "，" + end + "]";
            displayResult(str);
        }

        @Override
        // Callback method of a TTS event. eventName: event name. The events are as follows:
        // MLTtsConstants.EVENT_PLAY_RESUME: playback resumption.
        // MLTtsConstants.EVENT_PLAY_PAUSE: playback pause.
        // MLTtsConstants.EVENT_PLAY_STOP: playback stop.
        public void onEvent(String taskId, int eventName, Bundle bundle) {
            String str = "TaskID: " + taskId + ", eventName:" + eventName;
            // The bundle parameter is not empty only when the EVENT_PLAYSTOP event occurs.
            if (eventName == MLTtsConstants.EVENT_PLAY_STOP) {
                // false: TTS ends normally; true: TTS stops when stop or pause is called.
                str += " " + bundle.getBoolean(MLTtsConstants.EVENT_PLAY_STOP_INTERRUPTED);
            }
            displayResult(str);
        }
    };

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
        if (this.mlTtsEngine != null) {
            this.mlTtsEngine.shutdown();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_speak:
                mlTtsEngine = new MLTtsEngine(mlConfigs);
                mlTtsEngine.setTtsCallback(callback);
                String text = mEditText.getText().toString();
                // sourceText: text information to be synthesized. The value can contain a maximum of 500 characters.
                // TTS mode.
                // MLTtsEngine.QUEUE_APPEND: queuing mode. In this mode, multiple TTS events are executed in the call sequence.
                // MLTtsEngine.QUEUE_FLUSH: queue clear mode. In this mode, the TTS events to be executed are cleared,
                // the synthesis and playback events being executed are stopped, and the latest TTS event is executed.
                String id = mlTtsEngine.speak(text, MLTtsEngine.QUEUE_APPEND);
                displayResult("TaskID: " + id + " submit.");
                break;
            case R.id.btn_stop_speak:
                mlTtsEngine.stop();
                break;
            default:
                break;
        }
    }
}
