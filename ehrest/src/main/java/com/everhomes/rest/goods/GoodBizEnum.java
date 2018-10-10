package com.everhomes.rest.goods;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>p1 : p1</li>
 * <li>currentPMId : 管理公司id</li>
 * <li>currentProjectId : 当前项目id</li>
 * <li>appId : 应用的originId</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月9日
 */

public enum GoodBizEnum {

	/*********云打印**********/
	PRINT_PRINT(1, (byte)0, "print", "打印"), 
	PRINT_COPY(2,  (byte)0, "copy", "复印"),
	PRINT_SCAN(3,  (byte)0, "scan", "扫描"),
	NONE(-1,(byte)-1,"","");
	
	public static final Byte TYPE_SIYIN_PRINT = 0;
	public static final Byte TYPE_PARKING = 1;
	
	private Integer code; //唯一标识
	private Byte type; //业务类型 0-打印 1-停车
	private String identity; //标识
	private String name; //名称 

	private GoodBizEnum(Integer code, Byte type, String identity, String name) {
		this.code = code;
		this.type = type;
		this.identity = identity;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getCode() {
		return code;
	}

	public Byte getType() {
		return type;
	}

	public String getIdentity() {
		return identity;
	}

	public String getName() {
		return name;
	}
}
