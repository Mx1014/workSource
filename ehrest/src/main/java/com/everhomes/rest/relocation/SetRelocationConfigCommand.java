package com.everhomes.rest.relocation;


/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间Id</li>
 * <li>ownerType: 项目类型</li>
 * <li>ownerId: 项目Id</li>
 * <li>agreementFlag: 协议开关:0为无效，1为有效</li>
 * <li>agreementContent: 协议内容</li>
 * <li>tipsFlag: 提示开关:0为无效，1为有效</li>
 * <li>tipsContent: 提示内容</li>
 * </ul>
 */
public class SetRelocationConfigCommand {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Byte agreementFlag;
    private String agreementContent;
    private Byte tipsFlag;
    private String tipsContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getAgreementFlag() {
        return agreementFlag;
    }

    public void setAgreementFlag(Byte agreementFlag) {
        this.agreementFlag = agreementFlag;
    }

    public String getAgreementContent() {
        return agreementContent;
    }

    public void setAgreementContent(String agreementContent) {
        this.agreementContent = agreementContent;
    }

    public Byte getTipsFlag() {
        return tipsFlag;
    }

    public void setTipsFlag(Byte tipsFlag) {
        this.tipsFlag = tipsFlag;
    }

    public String getTipsContent() {
        return tipsContent;
    }

    public void setTipsContent(String tipsContent) {
        this.tipsContent = tipsContent;
    }
}
