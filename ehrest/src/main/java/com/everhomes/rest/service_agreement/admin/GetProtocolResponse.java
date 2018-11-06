// @formatter:off
package com.everhomes.rest.service_agreement.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>type: 协议类型</li>
 *     <li>content: 文本</li>
 *     <li>variables: 变量集合，请参考{@link ProtocolVariableDTO}</li>
 * </ul>
 */
public class GetProtocolResponse {

    private Byte type;

    private String content;

    @ItemType(ProtocolVariableDTO.class)
    private List<ProtocolVariableDTO> variables;

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

    public List<ProtocolVariableDTO> getVariables() {
        return variables;
    }

    public void setVariables(List<ProtocolVariableDTO> variables) {
        this.variables = variables;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
