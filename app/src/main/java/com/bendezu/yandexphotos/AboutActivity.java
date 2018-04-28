package com.bendezu.yandexphotos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView linkTextView = findViewById(R.id.tv_link);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.action_about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView photosLogo = findViewById(R.id.iv_logo_photos);
        photosLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                photosLogo.startAnimation(animation);
            }
        });

        CardView authorCard = findViewById(R.id.author_card);
        authorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.github_link);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
