// @formatter:off
package com.everhomes.rest.service_agreement.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 协议模板类型,请参考{@link ProtocolTemplateType}</li>
 * </ul>
 */
public class GetProtocolTemplateCommand {
    private Byte type;

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
