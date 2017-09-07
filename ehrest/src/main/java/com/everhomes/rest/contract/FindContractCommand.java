package com.everhomes.rest.contract;

/**
 * <ul>
 *     <li>id: 合同id</li>
 *     <li>contractNumber: 合同编号</li>
 *     <li>namespaceId: 域空间id</li>
 *     <li>communityId: 园区id</li>
 *     <li>partyAId: 合同甲方id</li>
 * </ul>
 * Created by ying.xiong on 2017/8/2.
 */
public class FindContractCommand {

    private Long id;

    private String contractNumber;

    private Integer namespaceId;

    private Long communityId;

    private Long partyAId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPartyAId() {
        return partyAId;
    }

    public void setPartyAId(Long partyAId) {
        this.partyAId = partyAId;
    }
}
