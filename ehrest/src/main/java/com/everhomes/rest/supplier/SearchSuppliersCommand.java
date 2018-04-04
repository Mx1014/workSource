//@formatter:off
package com.everhomes.rest.supplier;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>nameKeyword:名字关键子</li>
 * <li>communityId:园区id</li>
 *</ul>
 */
public class SearchSuppliersCommand {
    private String nameKeyword;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getNameKeyword() {
        return nameKeyword;
    }

    public void setNameKeyword(String nameKeyword) {
        this.nameKeyword = nameKeyword;
    }
}
