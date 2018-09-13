package com.everhomes.rest.organization.pm;


public enum PaymentChargingItemType {
	ERRORSTATUS((byte)-1, "无效状态"),
	
	ZUJIN((byte)1, "租金"),
	WUYEFEI((byte)2, "物业费"),
	YAJIN((byte)3, "押金"),
	ZIYONGSHUIFEI((byte)4, "自用水费"),
	ZIYONGDIANFEI((byte)5, "自用电费"),
	ZHINAJIN((byte)6, "滞纳金"),
	GONGTANSHUIFEI((byte)7, "公摊水费"),
	GONGTANDIANFEI((byte)8, "公摊电费"),
	ZENGZHIFUWUFEI((byte)9, "增值服务费"),
	SHUIJIN((byte)10, "税金"),
	DIANTIBAOYANGFEI((byte)11, "电梯保养费"),
	JIBENFEI((byte)12, "基本费"),
	FUWUFEI((byte)13, "服务费"),
	BAOJIEFEI((byte)14, "保洁费"),
	ANBAOFEI((byte)15, "安保费"),
	WUSHIFEI((byte)16, "污水费"),
	GUANLI((byte)17, "管理费"),
	KONGTIAO((byte)18, "空调费"),
	WANGLUO((byte)19, "网络费"),
	GUANGGAO((byte)20, "广告费");

    
    private byte code;
    private String desc;
    
    private PaymentChargingItemType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getDesc() {
		return this.desc;
	}

	public static PaymentChargingItemType fromCode(Byte code) {
       if (code != null) {
    	   for (PaymentChargingItemType status : PaymentChargingItemType.values()) {
			if (status.code == code.byteValue()) {
				return status;
			}
		}
       }
       return null;
    }
    
    public static PaymentChargingItemType fromDesc(String desc) {
    	if (desc != null && !desc.isEmpty()) {
			for (PaymentChargingItemType status : PaymentChargingItemType.values()) {
				if (status.desc.equals(desc)) {
					return status;
				}
			}
		}
    	return PaymentChargingItemType.ERRORSTATUS;
    }

	public static PaymentChargingItemType fromCode(Long code) {
		 if (code != null) {
	    	   for (PaymentChargingItemType status : PaymentChargingItemType.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
	       }
	       return null;
	}
}