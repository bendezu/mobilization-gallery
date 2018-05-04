package com.bendezu.yandexphotos.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bendezu.yandexphotos.R;
import com.bendezu.yandexphotos.gallery.GalleryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    @BindView(R.id.btn_log_in) View logInButton;

    private AuthContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        presenter = new AuthPresenter(this);

        ButterKnife.bind(this);

        if (savedInstanceState == null &&
                presenter.getAccessToken() != null){
            launchGallery();
        }
    }

    @OnClick(R.id.btn_log_in)
    public void logIn() {
        presenter.startAuthProcess(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        presenter.verifyAccess(intent);
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