//@formatter:off
package com.everhomes.rest.contract;

/**
 * Created by djm 2018/9/5 
 * 
 * 房源租金类型（1:每天(总面积); 2:每月(总面积); 3:每个季度(总面积); 4:每年(总面积);
 */
public enum ApartmentRentType {
	//总面积计算
	DAY((byte) 1, 30, false), 
	NATURAL_MONTH((byte) 2, 0, false), 
	NATURAL_QUARTER((byte) 3, 3, false),
	NATURAL_YEAR((byte) 4, 12, false),
	//每平米计算
	/*DAY_SQUARE_METRE((byte) 5, 30, true), 
	NATURAL_MONTH_SQUARE_METRE((byte) 6, 0, true), 
	NATURAL_QUARTER_SQUARE_METRE((byte) 7, 3, true), 
	NATURAL_YEAR_SQUARE_METRE((byte) 8, 12, true);*/
	
	CONTRACT_MONTH((byte)6, 0, false),//合同月
    CONTRACT_QUARTER((byte)7, 3, false),//合同季
    CONTRACT_YEAR((byte)8, 12, false),//合同年
	CONTRACT_TWOMONTH((byte)9, 2, false),//新增按合同两个月
	CONTRACT_SIXMONTH((byte)10, 6, false);//新增按合同6个月

	private byte code;
	private Integer offset;
	private boolean type;

	ApartmentRentType(byte code, Integer offset, boolean type) {
		this.code = code;
		this.offset = offset;
		this.type = type;
	}

	public byte getCode() {
		return this.code;
	}

	public Integer getOffset() {
		return this.offset;
	}

	public boolean isType() {
		return this.type;
	}

	public static ApartmentRentType fromCode(Byte code) {
		if (code == null)
			return null;
		for (ApartmentRentType status : ApartmentRentType.values()) {
			if (status.getCode() == code) {
				return status;
			}
		}
		return null;
	}
}
