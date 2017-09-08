//@formatter:off
package com.everhomes.rest.asset;

/**
 * Created by Wentian Wang on 2017/9/8.
 */
/**
 *<ul>
 * <li>contractId:合同id</li>
 * <li>contractNumber:合同编号</li>
 * <li>namespaceId:域空间</li>
 * <li>communityId:园区id</li>
 * <li>partyAId:第三方id</li>
 * <li>targetType:客户类型，个人eh_user,企业eh_organization</li>
 * <li>ownerType:所属者类型，园区为community</li>
 *</ul>
 */
public class GetAreaAndAddressByContractCommand {
    private String contractId;

    private String contractNumber;

    private Integer namespaceId;

    private Long communityId;

    private Long partyAId;

    private String targetType;

    private String ownerType;


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
