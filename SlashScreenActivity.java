package com.example.ruanzetao.pfoody.View;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ruanzetao.pfoody.R;

public class SlashScreenActivity extends AppCompatActivity {

    TextView txtPhienBan, txtTenCongTy,txtTai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_flashscreen);

        txtPhienBan=findViewById(R.id.txtPhienBan);
        //txtTenCongTy=findViewById(R.id.txtTenCongTy);

        try{
        PackageInfo packageInfo=getPackageManager().getPackageInfo(getPackageName(),0);
        txtPhienBan.setText(getString(R.string.phienban)+" "+packageInfo.versionName);

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent iDangNhap=new Intent(SlashScreenActivity.this,DangNhapActivity.class);
                    startActivity(iDangNhap);
                }
            },2000);

        }
        catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }
}
