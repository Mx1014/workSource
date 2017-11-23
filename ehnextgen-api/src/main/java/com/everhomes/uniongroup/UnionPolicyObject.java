package com.everhomes.uniongroup;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Set;
import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;

public class UnionPolicyObject {
    private List<UniongroupConfigures> configureList;
    private List<EhUniongroupMemberDetails> unionDetailsList;
    private Set<Long> detailIds;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<UniongroupConfigures> getConfigureList() {
        return configureList;
    }

    public void setConfigureList(List<UniongroupConfigures> configureList) {
        this.configureList = configureList;
    }

    public List<EhUniongroupMemberDetails> getUnionDetailsList() {
        return unionDetailsList;
    }

    public void setUnionDetailsList(List<EhUniongroupMemberDetails> unionDetailsList) {
        this.unionDetailsList = unionDetailsList;
    }

    public Set<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(Set<Long> detailIds) {
        this.detailIds = detailIds;
    }
}
