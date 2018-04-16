package com.bendezu.yandexphotos.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CloudApi {

    @GET("v1/disk/resources/last-uploaded")
    Call<ResourceList> getLastUploadedResources(
            @Query("limit") Integer limit, @Query("media_type") String mediaType,
            @Query("offset") Integer offset, @Query("fields") String fields,
            @Query("preview_size") String previewSize,
            @Query("preview_crop") Boolean previewCrop
    );

}
