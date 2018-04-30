package com.bendezu.yandexphotos.data;

public class ImageData {

    private String name;
    private String path;
    private String created;
    private String file;
    private String preview;

    public ImageData(String name, String path, String created, String file, String preview, String mimeType) {
        this.name = name;
        this.path = path;
        this.created = created;
        this.file = file;
        this.preview = preview;
        this.mimeType = mimeType;
    }

    private String mimeType;

    public String getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
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

}

