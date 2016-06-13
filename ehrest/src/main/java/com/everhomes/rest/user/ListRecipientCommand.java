package com.everhomes.rest.user;

/**
 * 查询邀请人列表
 * 
 * @author elians
 *         <ul>
 *         <li>anchor:id偏移值</li>
 *         </ul>
 */
public class ListRecipientCommand {
    private Long anchor;

    public Long getAnchor() {
        return anchor;
    }

    public void setAnchor(Long anchor) {
        this.anchor = anchor;
    }

}
