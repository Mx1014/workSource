package com.everhomes.rest.asset;
 /* @date 2018年12月7日----下午2:03:56
        */
/**
 *<ul>
  * <li>namespaceId:域空间ID</li>
  * <li>sourceType:业务系统类型</li>
  * <li>sourceId:业务系统ID</li>
  * <li>dateStr:账期</li>
  * <li>targetId:目标ID</li>
  * <li>targetType:目标类型</li>
  * <li>targetType:目标类型</li>
 *</ul>
 */
public class PaymentBillsCommand {

    private Integer namespaceId;

    private String sourceType;

    private Long sourceId;

    private String dateStr;

    private Long targetId;

    private String targetType;

    private Long billId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }
}
