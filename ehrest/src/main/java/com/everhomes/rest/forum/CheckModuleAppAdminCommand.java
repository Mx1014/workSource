// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>moduleType: 模块类型  参考 {@link ForumModuleType}</li>
 *     <li>categoryId: categoryId</li>
 *     <li>currentOrgId: currentOrgId</li>
 * </ul>
 */
public class CheckModuleAppAdminCommand {

    private Byte moduleType;
    private Long categoryId;
    private Long currentOrgId;

    public Byte getModuleType() {
        return moduleType;
    }

    public void setModuleType(Byte moduleType) {
        this.moduleType = moduleType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCurrentOrgId() {
        return currentOrgId;
    }

    public void setCurrentOrgId(Long currentOrgId) {
        this.currentOrgId = currentOrgId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
