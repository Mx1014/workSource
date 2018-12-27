// @formatter:off
package com.everhomes.rest.portal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId:域空间ID</li>
 *     <li>indexFlag: 是否启用主页签，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 * </ul>
 */
public class UpdateIndexFlagCommand {

    private Integer namespaceId;

    private Byte indexFlag;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(Byte indexFlag) {
        this.indexFlag = indexFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
