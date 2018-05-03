package com.bendezu.yandexphotos.util;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.bendezu.yandexphotos.util.UriUtils.getFragmentParameter;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UriUtilsTest {

    @Mock Uri uri;

    @Before public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegularUri() {
        String testFragment = "access_token=ABC123&expires_in=123456";
        when(uri.getFragment()).thenReturn(testFragment);

        String accessToken = getFragmentParameter(uri, "access_token");
        assertEquals("ABC123", accessToken);
        String expiresIn = getFragmentParameter(uri, "expires_in");
        assertEquals("123456", expiresIn);
    }

    @Test
    public void testUriWithNoFragment() {
        String testFragment = "";
        when(uri.getFragment()).thenReturn(testFragment);

        assertEquals(null, getFragmentParameter(uri, "some_key"));
    }

    @Test
    public void testWhenKeyWasNotFound() {
        String testFragment = "access_token=ABC123&expires_in=123456";
        when(uri.getFragment()).thenReturn(testFragment);

        assertEquals(null, getFragmentParameter(uri, "some_key"));
    }

}
