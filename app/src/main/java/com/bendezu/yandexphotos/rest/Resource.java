package com.bendezu.yandexphotos.rest;

import com.google.gson.annotations.SerializedName;

public class Resource {

    @SerializedName("file")
    String file;

    @SerializedName("name")
    String name;

    @SerializedName("created")
    String created;

    @SerializedName("resource_id")
    String resourceId;

    @SerializedName("modified")
    String modified;

    @SerializedName("preview")
    String preview;

    @SerializedName("path")
    String path;

    @SerializedName("mime_type")
    String mimeType;

    @SerializedName("size")
    int size;

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getModified() {
        return modified;
    }

    public String getPreview() {
        return preview;
    }

    public String getPath() {
        return path;
    }

    public String getMimeType() {
        return mimeType;
    }

    public int getSize() {
        return size;
    }
}
