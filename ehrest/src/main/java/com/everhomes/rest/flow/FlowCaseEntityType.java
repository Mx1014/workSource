package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>list: 列表类型</li>
 * <li>multi_line: 多行类型</li>
 * <li>text: 大段文字类型</li>
 * <li>image: 图片类型</li>
 * <li>file: 附件类型</li>
 *<li>ENTITY_GROUP("entity_group"): FlowCaseEntity 组间隔</li>
 * <li>SUB_ENTITY_GROUP("sub_entity_group"): FlowCaseEntity 子组间隔</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowCaseEntityType {
	LIST("list"),
	MULTI_LINE("multi_line"),
	TEXT("text"),
	IMAGE("image"),
	FILE("file"),
	CONTRACT_PRICE("contract_price"),
	ENTITY_GROUP("entity_group"),
	SUB_ENTITY_GROUP("sub_entity_group");

	private String code;

    private FlowCaseEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowCaseEntityType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowCaseEntityType t : FlowCaseEntityType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
