package com.everhomes.rest.enterprise;

/**
 * <ul> 管理后台功能：将企业入驻某园区
 * <li>enterpriseId: 企业ID</li>
 * <li>communityId: 园区ID</li>
 * </ul>
 * @author janson
 *
 */
public class EnterpriseJoinCommunityCommand {
    Long enterpriseId;
    Long communityId;
    
    public Long getEnterpriseId() {
        return enterpriseId;
    }
    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
    public Long getCommunityId() {
        return communityId;
    }
    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }
    
    
}
