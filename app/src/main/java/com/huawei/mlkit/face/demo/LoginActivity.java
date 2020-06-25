package com.huawei.mlkit.face.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.mlkit.face.demo.utils.FaceConstants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton, saveFaceButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Singleton.getInstance();
        Singleton.setCurrentActivity(this);
        initViews();
    }

    private void initViews() {
        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this);
        saveFaceButton = findViewById(R.id.buttonSaveFace);
        saveFaceButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonLogin:
                if(!Singleton.getSettings().getString(FaceConstants.USER_SESSION, "").isEmpty()){

                }else{
                    Toast.makeText(this,getResources().getString(R.string.not_face), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonSaveFace:
                Intent faceIntent = new Intent(LoginActivity.this, LiveImageDetectionActivity.class);
                startActivity(faceIntent);
                break;
        }
    }
}