package com.everhomes.rest.archives;

/**
 * <ul>
 * <li>namespaceId: 域空间id</li>
 * <li>detailId: 记录所属员工企业id</li>
 * <li>organizationId: 公司id</li>
 * <li>operationType: 操作类型</li>
 * <li>operationTime: 操作时间</li>
 * <li>str1: 记录字段1</li>
 * <li>...</li>
 * <li>str6: 记录字段6</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorName: 操作人名称</li>
 * </ul>
 */
public class AddArchivesLogCommand {

    private Integer namespaceId;

    private Long detailId;

    private Long organizationId;

    private Byte operationType;

    private String operationTime;

    private String str1;
    private String str2;
    private String str3;
    private String str4;
    private String str5;
    private String str6;

    private Long operatorUid;

    private String operatorName;

    public AddArchivesLogCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public String getStr5() {
        return str5;
    }

    public void setStr5(String str5) {
        this.str5 = str5;
    }

    public String getStr6() {
        return str6;
    }

    public void setStr6(String str6) {
        this.str6 = str6;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
