package com.everhomes.rest.enterprise;


import com.everhomes.util.StringHelper;

/**
 * <ul> 查询公司（标准版）
 * <li>namespaceId: 域空间</li>
 * <li>keywords: 关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 页面大小</li>
 * <li>type: 企业类型</li>
 * </ul>
 *
 */
public class ListEnterpriseByNamespaceIdCommand {
    private Integer namespaceId;
    private String  keywords;
    private Long pageAnchor;
    private Integer pageSize;
    private String type;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
