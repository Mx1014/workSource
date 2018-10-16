package com.everhomes.rest.service_agreement.admin;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 
 * <li>content: 文本</li>
 * <li>type: 协议模板类型,请参考{@link ProtocolTemplateType}</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>variables: 变量集合，请参考{@link ProtocolVariableDTO}</li>
 * </ul>
 */
public class SaveProtocolsCommand {
    private String content;
	private Byte type;
	private Integer namespaceId;
	@ItemType(ProtocolVariableDTO.class)
	private List<ProtocolVariableDTO> variables;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<ProtocolVariableDTO> getVariables() {
        return variables;
    }

    public void setVariables(List<ProtocolVariableDTO> variables) {
        this.variables = variables;
    }

    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
