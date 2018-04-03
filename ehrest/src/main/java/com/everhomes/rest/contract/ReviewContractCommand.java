package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>partyAId: 合同甲方id</li>
 *     <li>result: 结果 参考{@link com.everhomes.rest.contract.ContractStatus}</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class ReviewContractCommand {

    private Long id;

    private Long partyAId;

    private Byte result;

    private Long orgId;

    private Long communityId;

    private Integer namespaceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }

    public Byte getResult() {
        return result;
    }

    public void setResult(Byte result) {
        this.result = result;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
