package com.everhomes.rest.yellowPage.stat;

import com.everhomes.rest.wanke.ActionType;
import com.everhomes.util.StringHelper;
/**
 * 点击/排序类型
 * <ul>
 * <li>点击服务类型 1</li>
 * <li>查看详情 3</li>
 * <li>点击申请按钮 4</li>
 * <li>咨询 5</li>
 * <li>分享 6</li>
 * <li>提交申请 20</li>
 * <li>转化率 21</li>
 * </ul>
 */
public enum StatClickOrSortType {
	
	// 点击类型，也用于排序标识
	CLICK_TYPE_SERVICE_TYPE((byte)1, "点击服务类型"), 
	CLICK_TYPE_SERVICE((byte)3,  "查看详情"), 
	CLICK_TYPE_COMMIT_BUTTON((byte)4, "点击申请按钮"),
	CLICK_TYPE_SUPPORT((byte)5, "咨询"), 
	CLICK_TYPE_SHARE((byte)6,  "分享"), 
	CLICK_TYPE_COMMIT_TIMES((byte)20, "提交申请"),
	
	//用于排序
	SORT_TYPE_CONVERSION_PERCENT((byte)21, (byte)1, "转化率");
	
	public static final Byte CLICK_TYPE = 0;
	public static final Byte SORT_TYPE = 1;

	private Byte code;
	private Byte type; //0-点击类型 1-排序类型
	private String info; // 说明

	private StatClickOrSortType(Byte code, Byte type, String info) {
		this.type = type;
		this.code = code;
		this.info = info;
	}
	
	private StatClickOrSortType(Byte code,  String info) {
		this.type = (byte)0; 
		this.code = code;
		this.info = info;
	}
	
	public Byte getCode() {
		return code;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public Byte getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
    public static StatClickOrSortType fromCode(Byte code) {
        if(code == null)
            return null;
        
        StatClickOrSortType[] values = StatClickOrSortType.values();
        for(StatClickOrSortType value : values) {
            if(value.code == code.byteValue()) {
                return value;
            }
        }
        
        return null;
    }
}
