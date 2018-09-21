package com.example.ruanzetao.pfoody.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.ruanzetao.pfoody.R;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class DangNhapActivity extends AppCompatActivity
implements GoogleApiClient.OnConnectionFailedListener
            ,OnClickListener
            ,FirebaseAuth.AuthStateListener {
    Button btnDangNhapGoogle;
    GoogleApiClient apiClient;
    public static int REQUEST_DANGNHAP_GOOGLE = 99;
    public static int KIEMTRA_PROVIDER_DANGNHAP = 0;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.layout_dangnhap);
        firebaseAuth = FirebaseAuth.getInstance();
        //firebaseAuth.signOut();

        btnDangNhapGoogle = findViewById(R.id.btnDangNhapGoogle);
        btnDangNhapGoogle.setOnClickListener(this);

        TaoClientDangNhapGoogle();
    }

    private void TaoClientDangNhapGoogle() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //lấy token, email
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();

    }

    //Open form google sign in
    private void DangNhapGoogle(GoogleApiClient apiClient) {
        KIEMTRA_PROVIDER_DANGNHAP = 1;
        Intent iDNGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(iDNGoogle, REQUEST_DANGNHAP_GOOGLE);
    }
    //end

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
    }



    //begin lay token id da dang nhap bang google de dang nhap tren firebase
    private void ChungThucDangNhapFirebase(String tokenID) {

        if (KIEMTRA_PROVIDER_DANGNHAP == 1) {
            AuthCredential authCredential = GoogleAuthProvider
                    .getCredential(tokenID, null);
            firebaseAuth.signInWithCredential(authCredential);
        }

    }
    //end lay token id da dang nhap bang google de dang nhap tren firebase

    //begin lay ket qua dang nhap bang google

    //kiem tra trong luc thao tac, nhan token id, lay ket qua dang nhap cua google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DANGNHAP_GOOGLE) {
            if (resultCode == RESULT_OK) {
                GoogleSignInResult signInResult = Auth.GoogleSignInApi
                        .getSignInResultFromIntent(data);
                GoogleSignInAccount account = signInResult.getSignInAccount();
                String tokenID = account.getIdToken();
                ChungThucDangNhapFirebase(tokenID);
            }
        }
    }
    //end
    @Override

    public void onConnectionFailed (@NonNull ConnectionResult connectionResult){

    }


    //begin lang nghe event user click vao button dang nhap google or fb or email account
    @Override
    public void onClick (View view){

        int id = view.getId();
        switch (id) {
            case R.id.btnDangNhapGoogle:
                DangNhapGoogle(apiClient);
                break;
        }
    }
    //end lang nghe event user click vao button dang nhap google or fb or email account


    //event kiem tra user da dang nhap thanh cong hay that bai hay logout hay dang xuat
    @Override
    public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth){

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            Intent iTrangChu=new Intent(this,TrangChuActivity.class);
            startActivity(iTrangChu);
        }else{

        }
    }
    //end kiem tra user da dang nhap thanh cong hay that bai hay logout hay dang xuat

}