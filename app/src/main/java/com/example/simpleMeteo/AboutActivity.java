package com.example.simpleMeteo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;



public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    // ссылка для GitHub
    public void gitLink (View v){
        Uri uri = Uri.parse("https://github.com/PaddeusAA/SimpleMeteo");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //ссылка на tg
    public void tgLink (View v){
        Uri uri = Uri.parse("https://t.me/LinusTorvalds_337");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //кнопка перехода на основной слой
    public void buttSwitchToDesiredA(View v) {
        Intent intent = new Intent(this, DesiredCityActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}