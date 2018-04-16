package com.bendezu.yandexphotos.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResourceList {

    @SerializedName("items")
    List<Resource> items;

    @SerializedName("limit")
    int limit;

    public List<Resource> getItems() {
        return items;
    }

    public int getLimit() {
        return limit;
    }
}
