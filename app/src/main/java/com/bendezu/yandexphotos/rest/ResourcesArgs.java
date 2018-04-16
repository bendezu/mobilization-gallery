package com.bendezu.yandexphotos.rest;

public class ResourcesArgs {

    private String fields, previewSize, mediaType;
    private Integer limit, offset;
    private Boolean previewCrop;

    private ResourcesArgs(String fields, String previewSize, String mediaType,
                          Integer limit, Integer offset, Boolean previewCrop) {
        this.fields = fields;
        this.previewSize = previewSize;
        this.mediaType = mediaType;
        this.limit = limit;
        this.offset = offset;
        this.previewCrop = previewCrop;
    }

    public String getFields() {
        return fields;
    }

    public String getPreviewSize() {
        return previewSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public Boolean getPreviewCrop() {
        return previewCrop;
    }

    public static class Builder {

        private String fields, previewSize, mediaType;
        private Integer limit, offset;
        private Boolean previewCrop;

        public ResourcesArgs build() {
            return new ResourcesArgs(fields, previewSize, mediaType, limit, offset, previewCrop);
        }

        public ResourcesArgs.Builder setFields(String fields) {
            this.fields = fields;
            return this;
        }

        public ResourcesArgs.Builder setPreviewSize(String previewSize) {
            this.previewSize = previewSize;
            return this;
        }

        public ResourcesArgs.Builder setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public ResourcesArgs.Builder setOffset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public ResourcesArgs.Builder setPreviewCrop(Boolean previewCrop) {
            this.previewCrop = previewCrop;
            return this;
        }

        public ResourcesArgs.Builder setMediaType(String mediaType) {
            this.mediaType = mediaType;
            return this;
        }
    }
}
