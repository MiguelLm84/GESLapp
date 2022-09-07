package com.example.geslapp.ui;


import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geslapp.R;

import java.io.File;


public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File file = new File(PATH);

        File outputFile = new File(file, "geslapp.apk");
        if (outputFile.exists()) {
            outputFile.delete();

        }

        //Ir a LoginActivity
        goToLogin();

       // Toast.makeText(this, "Se ha borrado el archivo antiguo:"+outputFile.exists(), Toast.LENGTH_SHORT).show();
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Thread.sleep(2000);
                    Intent t = new Intent(Splash.this, LoginActivity.class);
                    startActivity(t);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    //Método para ir al LoginActivity
    private void goToLogin() {

        new Handler().postDelayed(() -> {
            Intent i = new Intent(Splash.this, LoginActivity.class);
            startActivity(i);
            finish();
        }, 2000);
    }
}
