package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>id: 主键</li>
 * <li>namespaceId: 域空间Id</li>
 * <li>ownerType: 归属的类型</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>taskCategoryId: 应用类型：6为物业报修（1为正中会报修），9为投诉建议</li>
 * <li>paymentFlag: 费用清单在线支付启用标识 0:禁用 1:启用</li>
 * <li>paymentAccount: 收款方账号</li>
 * <li>paymentAccountType: 收款方类型</li>
 * <li>contentHint: 服务内容提示文本</li>
 * </ul>
 */
public class SetPmTaskConfigCommand {

    private Long id;
    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long taskCategoryId;
    private Byte paymentFlag;
    private Long paymentAccount;
    private String paymentAccountType;
    private String contentHint;

    private Long appId;

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

    public Byte getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(Byte paymentFlag) {
        this.paymentFlag = paymentFlag;
    }

    public Long getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(Long paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getPaymentAccountType() {
        return paymentAccountType;
    }

    public void setPaymentAccountType(String paymentAccountType) {
        this.paymentAccountType = paymentAccountType;
    }

    public String getContentHint() {
        return contentHint;
    }

    public void setContentHint(String contentHint) {
        this.contentHint = contentHint;
    }

    public Long getTaskCategoryId() {
        return taskCategoryId;
    }

    public void setTaskCategoryId(Long taskCategoryId) {
        this.taskCategoryId = taskCategoryId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
