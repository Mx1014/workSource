package com.everhomes.rest.policy;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>categoryId: 企业类型 不传代表全选</li>
 * <li>beginDate: 开始日期</li>
 * <li>endDate: 结束日期</li>
 * <li>keyWord: 关键字（用户姓名/联系电话/企业名称）</li>
 * <li>pageAnchor: 分页瞄</li>
 * <li>pageSize: 每页条数</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>appId: 应用ID</li>
 * </ul>
 */
public class GetPolicyRecordCommand {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private Long categoryId;
    private Long beginDate;
    private Long endDate;
    private String keyWord;

    private Long pageAnchor;
    private Integer pageSize;

    private Long currentPMId;
    private Long appId;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCurrentPMId() {
        return currentPMId;
    }

    public void setCurrentPMId(Long currentPMId) {
        this.currentPMId = currentPMId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
