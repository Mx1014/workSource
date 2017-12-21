// @formatter:off
package com.everhomes.rest.forum;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.hotTag.TagDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: namespaceId</li>
 *     <li>moduleType: 模块类型  参考 {@link ForumModuleType}</li>
 *     <li>categoryId: 模块入口id，活动、论坛等多入口的模块需要传</li>
 *     <li>interactFlag: 是否支持交互 {@link InteractFlag}</li>
 * </ul>
 */
public class UpdateInteractSettingCommand {

    private Integer namespaceId;

    private Byte moduleType;

    private Long categoryId;

    private Byte interactFlag;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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

    public Byte getInteractFlag() {
        return interactFlag;
    }

    public void setInteractFlag(Byte interactFlag) {
        this.interactFlag = interactFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
