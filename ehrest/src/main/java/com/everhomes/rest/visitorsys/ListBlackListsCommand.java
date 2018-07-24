// @formatter:off
package com.everhomes.rest.visitorsys;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: (必填)域空间id</li>
 * <li>ownerType: (必填)归属的类型，{@link com.everhomes.rest.visitorsys.VisitorsysOwnerType}</li>
 * <li>ownerId: (必填)归属的ID,园区/公司的ID</li>
 * <li>appId: (必填)应用Id</li>
 * <li>keyWords: (选填)黑名单信息,访客姓名手机号码模糊搜索</li>
 * <li>pageAnchor: (选填)锚点</li>
 * <li>pageSize: (选填)每页的数量</li>
 * </ul>
 */
public class ListBlackListsCommand extends BaseVisitorsysCommand{
    private String keyWords;
    private Long pageAnchor;
    private Integer pageSize;

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
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
