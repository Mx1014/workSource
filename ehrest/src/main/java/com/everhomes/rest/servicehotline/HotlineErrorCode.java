package com.everhomes.rest.servicehotline;

/**
 * @author 黄明波
 */
public enum HotlineErrorCode {
	
	ERROR_DUPLICATE_PHONE(10001, "hotline or servicer already exists", "相同号码已存在"), 
	ERROR_HOTLINE_SERVICER_KEY_INVALID(10002, "servicer not specify", "查询记录时未指定客服或热线"), 
	ERROR_HOTLINE_CUSTOMER_KEY_INVALID(10003, "customer not specify", "查询记录时未指定用户"),
	ERROR_HOTLINE_UPDATING_NOT_EXIST(10004, "record being updated or deleted not exist", "需要更新/删除的记录不存在");
	
	public static String SCOPE = "hotline"; //eh_locale_strings的标识
    
    private int code; 
    private String info; //英文报错信息
    private String comment; //中文注释 需在eh_locale_strings中配置
    
    private HotlineErrorCode(int code, String info, String comment) {
        this.code = code;
        this.info = info;
        this.comment = comment;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public static HotlineErrorCode fromCode(String code) {
        if(code != null) {
        	HotlineErrorCode[] values = HotlineErrorCode.values();
            for(HotlineErrorCode value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }

}
