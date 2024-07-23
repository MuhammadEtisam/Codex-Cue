package com.example.flashlight;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private ImageButton flashlightButton;
    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashlightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashlightButton = findViewById(R.id.flashlightButton);
        cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 50);
                } else {
                    toggleFlashlight(v); // Pass 'v' here to provide the required View parameter
                }
            }
        });
    }

    private void toggleFlashlight(View view) {
        try {
            if (isFlashlightOn) {
                cameraManager.setTorchMode(cameraId, false);
                isFlashlightOn = false;
                flashlightButton.setImageResource(R.drawable.flashlight_off);
                showToast("Flashlight turned off");
            } else {
                cameraManager.setTorchMode(cameraId, true);
                isFlashlightOn = true;
                flashlightButton.setImageResource(R.drawable.flashlight_on);
                showToast("Flashlight turned on");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 50) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleFlashlight(null); // Pass null or any View if needed
            } else {
                showToast("Permission Denied");
            }
        }
    }
}
