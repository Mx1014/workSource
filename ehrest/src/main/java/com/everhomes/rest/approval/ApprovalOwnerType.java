// @formatter:off
package com.everhomes.rest.approval;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <ul>审批所属者类型
 * <li>ORGANIZATION: organization，组织</li>
 * </ul>
 */
public enum ApprovalOwnerType {
	ORGANIZATION("organization");
	
	private String code;
	
	private ApprovalOwnerType(String code){
		this.code = code;
	}
	
	public static ApprovalOwnerType fromCode(String code){
		if (StringUtils.isNotBlank(code)) {
			for (ApprovalOwnerType absentOwnerType : ApprovalOwnerType.values()) {
				if (absentOwnerType.getCode().equals(code)) {
					return absentOwnerType;
				}
			}
		}
		return null;
	}
	
	public String getCode(){
		return this.code;
	}
}
