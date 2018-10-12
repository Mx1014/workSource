// @formatter:off
package com.everhomes.rest.service_agreement.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>type: 协议类型</li>
 *     <li>content: 文本</li>
 *     <li>variables: 变量集合，请参考{@link com.everhomes.rest.service_agreement.admin.ProtocolTemplateVariableDTO}</li>
 * </ul>
 */
public class GetProtocolTemplateResponse {

    private Byte type;

    private String content;

    @ItemType(ProtocolTemplateVariableDTO.class)
    private List<ProtocolTemplateVariableDTO> variables;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ProtocolTemplateVariableDTO> getVariables() {
        return variables;
    }

    public void setVariables(List<ProtocolTemplateVariableDTO> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
