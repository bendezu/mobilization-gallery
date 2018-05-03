package com.bendezu.yandexphotos.gallery;

import com.bendezu.yandexphotos.data.GalleryAsyncLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class GalleryFragmentPresenterTest {

    @Mock GalleryContract.View galleryView;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldShowMessage_whenNoConnection() {
        GalleryFragmentPresenter galleryFragmentPresenter = new GalleryFragmentPresenter(galleryView);

        galleryFragmentPresenter.processResponseStatus(GalleryAsyncLoader.NO_INTERNET_CONNECTION);

        verify(galleryView).showNoConnectionMessage();
        verify(galleryView).setSwipeRefreshing(false);
    }
}
