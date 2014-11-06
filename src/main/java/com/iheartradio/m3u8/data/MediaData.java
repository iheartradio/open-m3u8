package com.iheartradio.m3u8.data;

import java.util.List;

public class MediaData {
    private final MediaType mType;
    private final String mUri;
    private final String mGroupId;
    private final String mLanguage;
    private final String mAssociatedLanguage;
    private final String mName;
    private final boolean mDefault;
    private final boolean mAutoSelect;
    private final boolean mForced;
    private final String mInStreamId;
    private final List<String> mCharacteristics;

    private MediaData(
            MediaType type,
            String uri,
            String groupId,
            String language,
            String associatedLanguage,
            String name,
            boolean isDefault,
            boolean isAutoSelect,
            boolean isForced,
            String inStreamId,
            List<String> characteristics) {
        mType = type;
        mUri = uri;
        mGroupId = groupId;
        mLanguage = language;
        mAssociatedLanguage = associatedLanguage;
        mName = name;
        mDefault = isDefault;
        mAutoSelect = isAutoSelect;
        mForced = isForced;
        mInStreamId = inStreamId;
        mCharacteristics = characteristics;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MediaData)) {
            return false;
        }

        MediaData other = (MediaData) o;

        return this.mType == other.mType &&
               ObjectUtil.equals(this.mUri, other.mUri) &&
               ObjectUtil.equals(this.mUri, other.mUri) &&
               ObjectUtil.equals(this.mGroupId, other.mGroupId) &&
               ObjectUtil.equals(this.mLanguage, other.mLanguage) &&
               ObjectUtil.equals(this.mAssociatedLanguage, other.mAssociatedLanguage) &&
               ObjectUtil.equals(this.mName, other.mName) &&
               this.mDefault == other.mDefault &&
               this.mAutoSelect == other.mAutoSelect &&
               this.mForced == other.mForced &&
               ObjectUtil.equals(this.mInStreamId, other.mInStreamId) &&
               ObjectUtil.equals(this.mCharacteristics, other.mCharacteristics);
    }

    public MediaType getType() {
        return mType;
    }

    public boolean hasUri() {
        return mUri != null;
    }

    public String getUri() {
        return mUri;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public boolean hasLanguage() {
        return mLanguage != null;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public boolean hasAssociatedLanguage() {
        return mAssociatedLanguage != null;
    }

    public String getAssociatedLanguage() {
        return mAssociatedLanguage;
    }

    public String getName() {
        return mName;
    }

    public boolean isDefault() {
        return mDefault;
    }

    public boolean isAutoSelect() {
        return mAutoSelect;
    }

    public boolean isForced() {
        return mForced;
    }

    public boolean hasInStreamId() {
        return mInStreamId != null;
    }

    public String getInStreamId() {
        return mInStreamId;
    }

    public boolean hasCharacteristics() {
        return mCharacteristics != null;
    }

    public List<String> getCharacteristics() {
        return mCharacteristics;
    }

    public static class Builder {
        private MediaType mType;
        private String mUri;
        private String mGroupId;
        private String mLanguage;
        private String mAssociatedLanguage;
        private String mName;
        private boolean mDefault;
        private boolean mAutoSelect;
        private boolean mForced;
        private String mInStreamId;
        private List<String> mCharacteristics;
        private boolean mAutoSelectSet;
        private boolean mForcedSet;

        public Builder withType(MediaType type) {
            if (type == null) {
                throw new IllegalArgumentException("type is null");
            }

            mType = type;
            return this;
        }

        public Builder withUri(String uri) {
            mUri = uri;
            return this;
        }

        public Builder withGroupId(String groupId) {
            mGroupId = groupId;
            return this;
        }

        public Builder withLanguage(String language) {
            mLanguage = language;
            return this;
        }

        public Builder withAssociatedLanguage(String associatedLanguage) {
            mAssociatedLanguage = associatedLanguage;
            return this;
        }

        public Builder withName(String name) {
            mName = name;
            return this;
        }

        public Builder withDefault(boolean isDefault) {
            mDefault = isDefault;
            return this;
        }

        public Builder withAutoSelect(boolean isAutoSelect) {
            mAutoSelect = isAutoSelect;
            mAutoSelectSet = true;
            return this;
        }

        public Builder withForced(boolean isForced) {
            mForced = isForced;
            mForcedSet = true;
            return this;
        }

        public Builder withInStreamId(String inStreamId) {
            mInStreamId = inStreamId;
            return this;
        }

        public Builder withCharacteristics(List<String> characteristics) {
            mCharacteristics = characteristics;
            return this;
        }

        public boolean isAutoSelectSet() {
            return mAutoSelectSet;
        }

        public boolean isForcedSet() {
            return mForcedSet;
        }

        public MediaData build() {
            if (mType == null) {
                throw new IllegalStateException("MediaData requires a type");
            }

            if (mGroupId == null) {
                throw new IllegalStateException("MediaData requires a group ID");
            }

            if (mName == null) {
                throw new IllegalStateException("MediaData requires a name");
            }

            return new MediaData(
                    mType,
                    mUri,
                    mGroupId,
                    mLanguage,
                    mAssociatedLanguage,
                    mName,
                    mDefault,
                    mAutoSelect,
                    mForced,
                    mInStreamId,
                    mCharacteristics);
        }
    }
}
