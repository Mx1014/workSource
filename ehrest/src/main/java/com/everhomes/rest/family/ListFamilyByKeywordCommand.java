// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>keyword: 关键字</li>
 * </ul>
 */
public class ListFamilyByKeywordCommand {
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ListFamilyByKeywordCommand () {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
