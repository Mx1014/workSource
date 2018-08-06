package com.everhomes.rest.general_approval;

/**
 * <ul> 获取表单值
 * <li>formOriginId: 表单formOriginId</li>
 * <li>formVersion: 表单formVersion</li>
 * </ul>
 */
public class GetGeneralFormPrintTemplateCommand {
    private Long formOriginId;
    private Long formVersion;

    public GetGeneralFormPrintTemplateCommand() {
		super();
	}

	public GetGeneralFormPrintTemplateCommand(Long formOriginId, Long formVersion) {
		super();
		this.formOriginId = formOriginId;
		this.formVersion = formVersion;
	}

    public Long getFormOriginId() {
        return formOriginId;
    }

    public void setFormOriginId(Long formOriginId) {
        this.formOriginId = formOriginId;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }
}
