package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class EncryptionData {
    private final EncryptionMethod mMethod;
    private final String mUri;
    private final List<Byte> mInitializationVector;
    private final String mKeyFormat;
    private final List<Integer> mKeyFormatVersions;

    private EncryptionData(EncryptionMethod method, String uri, List<Byte> initializationVector, String keyFormat, List<Integer> keyFormats) {
        mMethod = method;
        mUri = uri;
        mInitializationVector = initializationVector == null ? null : Collections.unmodifiableList(initializationVector);
        mKeyFormat = keyFormat;
        mKeyFormatVersions = keyFormats == null ? null : Collections.unmodifiableList(keyFormats);
    }

    public EncryptionMethod getMethod() {
        return mMethod;
    }

    public boolean hasUri() {
        return mUri != null;
    }

    public String getUri() {
        return mUri;
    }

    public boolean hasInitializationVector() {
        return mInitializationVector != null;
    }

    public List<Byte> getInitializationVector() {
        return mInitializationVector;
    }

    public boolean hasKeyFormat() {
        return mKeyFormat != null;
    }

    public String getKeyFormat() {
        return mKeyFormat;
    }

    public boolean hasKeyFormatVersions() {
        return mKeyFormatVersions != null;
    }

    public List<Integer> getKeyFormatVersions() {
        return mKeyFormatVersions;
    }

    public static class Builder {
        private EncryptionMethod mMethod;
        private String mUri;
        private List<Byte> mInitializationVector;
        private String mKeyFormat;
        private List<Integer> mKeyFormatVersions;

        public Builder withMethod(EncryptionMethod method) {
            mMethod = method;
            return this;
        }

        public Builder withUri(String uri) {
            mUri = uri;
            return this;
        }

        public Builder withInitializationVector(List<Byte> initializationVector) {
            mInitializationVector = initializationVector;
            return this;
        }

        public Builder withKeyFormat(String keyFormat) {
            mKeyFormat = keyFormat;
            return this;
        }

        public Builder withKeyFormatVersions(List<Integer> keyFormatVersions) {
            mKeyFormatVersions = keyFormatVersions;
            return this;
        }

        public EncryptionData build() {
            if (mMethod == null) {
                throw new IllegalStateException("EncryptionData requires a method");
            }

            return new EncryptionData(mMethod, mUri, mInitializationVector, mKeyFormat, mKeyFormatVersions);
        }
    }
}
