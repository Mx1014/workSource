package com.everhomes.rest.general_approval;

/**
 * <ul> 获取表单值
 * <li>sourceType: 业务类型</li>
 * <li>sourceId: 业务ID</li>
 * <li>originFieldFlag: 字段原始名字是否返回</li>
 * </ul>
 */
public class GetGeneralFormValuesCommand {
    private String sourceType;
    private Long sourceId;

    private Byte originFieldFlag;

    public GetGeneralFormValuesCommand() {
		super();
	}

	public GetGeneralFormValuesCommand(String sourceType, Long sourceId, Byte originFieldFlag) {
		super();
		this.sourceType = sourceType;
		this.sourceId = sourceId;
		this.originFieldFlag = originFieldFlag;
	}

	public Byte getOriginFieldFlag() {
        return originFieldFlag;
    }

    public void setOriginFieldFlag(Byte originFieldFlag) {
        this.originFieldFlag = originFieldFlag;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}
