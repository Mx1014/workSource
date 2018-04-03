package com.everhomes.uniongroup;

/**
 * Created by wuhan on 2017/10/19.
 */
public interface UniongroupVersionProvider {
    void createUniongroupVersion(UniongroupVersion uniongroupVersion);

    void updateUniongroupVersion(UniongroupVersion UniongroupVersion);

    UniongroupVersion findUniongroupVersion(Long enterpriseId, String groupType);
}
