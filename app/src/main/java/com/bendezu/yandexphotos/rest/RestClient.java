package com.bendezu.yandexphotos.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private final String SERVER_URL = "https://cloud-api.yandex.net/";

    private final CloudApi cloudApi;

    private String token;

    public RestClient(final String token) {
        this.token = token;

        cloudApi = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CloudApi.class);
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", "OAuth " + token)
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .build();
    }

    public Call<ResourceList> getLastUploadedResources(ResourcesArgs args){
        return cloudApi.getLastUploadedResources(args.getLimit(), args.getMediaType(),
                args.getOffset(), args.getFields(), args.getPreviewSize(), args.getPreviewCrop());
    }

}
