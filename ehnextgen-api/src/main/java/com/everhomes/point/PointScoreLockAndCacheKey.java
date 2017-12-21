package com.everhomes.point;

/**
 * Created by xq.tian on 2017/12/6.
 */
public class PointScoreLockAndCacheKey {

    Integer namespaceId;
    Long systemId, uid;

    PointScoreLockAndCacheKey(Integer namespaceId, Long systemId, Long uid) {
        this.namespaceId = namespaceId;
        this.systemId = systemId;
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointScoreLockAndCacheKey that = (PointScoreLockAndCacheKey) o;

        if (namespaceId != null ? !namespaceId.equals(that.namespaceId) : that.namespaceId != null) return false;
        if (systemId != null ? !systemId.equals(that.systemId) : that.systemId != null) return false;
        return uid != null ? uid.equals(that.uid) : that.uid == null;
    }

    @Override
    public int hashCode() {
        int result = namespaceId != null ? namespaceId.hashCode() : 0;
        result = 31 * result + (systemId != null ? systemId.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PointScoreLockAndCacheKey{" +
                "namespaceId=" + namespaceId +
                ", systemId=" + systemId +
                ", uid=" + uid +
                '}';
    }
}
