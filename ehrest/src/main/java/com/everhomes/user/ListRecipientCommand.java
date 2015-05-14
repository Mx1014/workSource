package com.everhomes.user;
/**
 * 查询邀请人列表
 * @author elians
 *<ul>
 *<li>pageOffset:页码偏移</li>
 *</ul>
 */
public class ListRecipientCommand {
    private Integer pageOffset;

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

}
