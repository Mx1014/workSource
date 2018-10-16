// @formatter:off
package com.everhomes.rest.service_agreement.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 协议模板类型,请参考{@link ProtocolTemplateType}</li>
 * <li>namespaceId: 域空间ID</li>
 * </ul>
 */
public class GetProtocolCommand {
    private Byte type;

    private Integer namespaceId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
