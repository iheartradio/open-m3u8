package com.iheartradio.m3u8.data;

import java.util.List;
import java.util.Objects;

public class MediaData {
    public static final int NO_CHANNELS = -1;

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
    private final int mChannels;

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
            List<String> characteristics,
            int channels) {
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
        mCharacteristics = DataUtil.emptyOrUnmodifiable(characteristics);
        mChannels = channels;
    }

    public MediaType getType() {
        return mType;
    }

    public boolean hasUri() {
        return mUri != null && !mUri.isEmpty();
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
        return !mCharacteristics.isEmpty();
    }

    public List<String> getCharacteristics() {
        return mCharacteristics;
    }

    public boolean hasChannels() {
        return mChannels != NO_CHANNELS;
    }

    public Integer getChannels() {
        return mChannels;
    }

    public Builder buildUpon() {
        return new Builder(
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
                mCharacteristics,
                mChannels);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
                mAssociatedLanguage,
                mAutoSelect,
                mCharacteristics,
                mDefault,
                mForced,
                mGroupId,
                mInStreamId,
                mLanguage,
                mName,
                mType,
                mUri,
                mChannels);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MediaData)) {
            return false;
        }

        MediaData other = (MediaData) o;

        return mType == other.mType &&
               Objects.equals(mUri, other.mUri) &&
               Objects.equals(mGroupId, other.mGroupId) &&
               Objects.equals(mLanguage, other.mLanguage) &&
               Objects.equals(mAssociatedLanguage, other.mAssociatedLanguage) &&
               Objects.equals(mName, other.mName) &&
               mDefault == other.mDefault &&
               mAutoSelect == other.mAutoSelect &&
               mForced == other.mForced &&
               Objects.equals(mInStreamId, other.mInStreamId) &&
               Objects.equals(mCharacteristics, other.mCharacteristics) &&
               mChannels == other.mChannels;
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
        private int mChannels = NO_CHANNELS;

        public Builder() {
        }

        private Builder(
                MediaType type,
                String uri,
                String groupId,
                String language,
                String associatedLanguage,
                String name,
                boolean isDefault,
                boolean autoSelect,
                boolean forced,
                String inStreamId,
                List<String> characteristics,
                int channels) {
            mType = type;
            mUri = uri;
            mGroupId = groupId;
            mLanguage = language;
            mAssociatedLanguage = associatedLanguage;
            mName = name;
            mDefault = isDefault;
            mAutoSelect = autoSelect;
            mForced = forced;
            mInStreamId = inStreamId;
            mCharacteristics = characteristics;
            mChannels = channels;
        }

        public Builder withType(MediaType type) {
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
            return this;
        }

        public Builder withForced(boolean isForced) {
            mForced = isForced;
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

        public Builder withChannels(int channels) {
            mChannels = channels;
            return this;
        }

        public MediaData build() {
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
                    mCharacteristics,
                    mChannels);
        }
    }

    @Override
    public String toString() {
        return "MediaData [mType=" + mType + ", mUri=" + mUri + ", mGroupId="
                + mGroupId + ", mLanguage=" + mLanguage
                + ", mAssociatedLanguage=" + mAssociatedLanguage + ", mName="
                + mName + ", mDefault=" + mDefault + ", mAutoSelect="
                + mAutoSelect + ", mForced=" + mForced + ", mInStreamId="
                + mInStreamId + ", mCharacteristics=" + mCharacteristics
                + ", mChannels=" + mChannels + "]";
    }
}
