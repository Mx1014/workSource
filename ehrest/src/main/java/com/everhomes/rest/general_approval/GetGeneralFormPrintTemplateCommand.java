package com.everhomes.rest.general_approval;

/**
 * <ul> 获取表单打印模板
 * <li>namespaceId: 域空间ID</li>
 * <li>formOriginId: 表单formOriginId</li>
 * <li>formVersion: 表单formVersion</li>
 * </ul>
 */
public class GetGeneralFormPrintTemplateCommand {
    private Integer namespaceId;
    private Long formOriginId;

    public GetGeneralFormPrintTemplateCommand() {
		super();
	}

	public GetGeneralFormPrintTemplateCommand(Long formOriginId, Long formVersion) {
		super();
		this.formOriginId = formOriginId;
	}

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }
}
