package com.bendezu.yandexphotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.tv_link) TextView linkTextView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.iv_logo_photos) ImageView photosLogo;
    @BindView(R.id.author_card) CardView authorCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);

        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        toolbar.setTitle(R.string.action_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.iv_logo_photos)
    public void rotateLogo() {

        Animation animation = AnimationUtils.
                loadAnimation(AboutActivity.this, R.anim.rotation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                photosLogo.setClickable(false);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                photosLogo.setClickable(true);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        photosLogo.startAnimation(animation);
    }

    @OnClick(R.id.author_card)
    public void openGithubLink() {

        String url = getString(R.string.github_link);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
