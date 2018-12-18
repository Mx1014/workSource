package com.everhomes.rest.flow;

/**
 * <ul>
 *     <li>DIRECT_RELATION((byte) 1): 直接关联表单,对应 {@link FlowFormRelationDataDirectRelation}</li>
 *     <li>CUSTOMIZE_FIELD((byte) 2): 关联自定义字段, 对应 {@link com.everhomes.rest.flow.FlowFormRelationDataCustomizeField}</li>
 * </ul>
 */
public enum FlowFormRelationType {

	DIRECT_RELATION((byte) 1), CUSTOMIZE_FIELD((byte) 2);

	private Byte code;

	FlowFormRelationType(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return this.code;
	}

	public static FlowFormRelationType fromCode(Byte code) {
		if (code == null) {
			return null;
		}

		for (FlowFormRelationType t : FlowFormRelationType.values()) {
			if (code.equals(t.getCode())) {
				return t;
			}
		}
		return null;
	}
}
