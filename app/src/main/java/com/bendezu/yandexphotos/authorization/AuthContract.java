package com.bendezu.yandexphotos.authorization;

import android.content.Context;
import android.content.Intent;

public interface AuthContract {

    interface View {

        void showErrorToast();
        void launchGallery();
    }

    interface Presenter {

        void startAuthProcess(Context context);
        void verifyAccess(Intent intent, Context context);

        String getAccessToken(Context context);
    }

}
