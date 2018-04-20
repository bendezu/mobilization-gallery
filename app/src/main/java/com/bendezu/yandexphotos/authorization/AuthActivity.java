package com.bendezu.yandexphotos.authorization;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.gallery.GalleryActivity;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    private AuthContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        presenter = new AuthPresenter(this);

        View logInButton = findViewById(R.id.btn_log_in);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.startAuthProcess(AuthActivity.this);
            }
        });

        if (savedInstanceState == null &&
                presenter.getAccessToken(this) != null){
            launchGallery();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        presenter.verifyAccess(intent, this);
        super.onNewIntent(intent);
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(this, R.string.access_denied, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void launchGallery() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
        finish();
    }
}