package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId : 域</li>
 * <li>enterpriseId : 企业Id</li>
 * <li>departmentId: 部门Id</li>
 * <li>groupId: 薪酬组Id</li>
 * <li>keyword: 内容关键字</li>
 * <li>pageAnchor: 本页开始锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchUniongroupDetailCommand {
    private Integer namespaceId;
    private Long enterpriseId;
    private Long departmentId;
    private Long groupId;
    private String keyword;
    private Long pageAnchor;
    private Integer pageSize;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
