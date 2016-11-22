package com.everhomes.rest.approval;

/**
 * 
 * <ul>时间范围类型：
 * <li>approval: 打卡</li>
 * <li>punch: 审批</li> >
 * </ul>
 */
public enum ListTargetType {
	APPROVAL("approval"), PUNCH("punch") ;

	private String code;
	
	private ListTargetType(String code) {
		this.code = code;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public ListTargetType fromCode(String code){
		if (code != null) {
			for (ListTargetType timeRangeType : ListTargetType.values()) {
				if (timeRangeType.getCode().equals(code)) {
					return timeRangeType;
				}
			}
		}
		return null;
	}
}
