package com.bendezu.yandexphotos.authorization;

import android.content.Intent;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthPresenterTest {

    @Mock AuthContract.View authView;
    @Mock Uri uri;
    @Mock Intent intent;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldShowMessage_whenAccessDenied() {
        AuthPresenter authPresenter = new AuthPresenter(authView);

        when(uri.getFragment()).thenReturn("error=access_denied");
        when(uri.toString()).thenReturn("yandexphotos://token#error=access_denied");
        when(intent.getData()).thenReturn(uri);

        authPresenter.verifyAccess(intent);

        verify(authView).showErrorToast();
    }

}
