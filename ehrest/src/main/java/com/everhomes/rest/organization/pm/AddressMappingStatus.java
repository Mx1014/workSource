package com.everhomes.rest.organization.pm;

/**
 *<ul>
 * 	<li>DEFAULT : 0, 其他</li>
 *	<li>LIVING : 1, 自用</li>
 *	<li>RENT : 2, 出租</li>
 *	<li>FREE : 3, 待租</li>
 *	<li>SALED : 4, 已售</li>
 *	<li>UNSALE : 5, 待售</li>
 *	<li>OCCUPIED : 6, 已预订</li>
 *	<li>SIGNEDUP : 7, 待签约</li>
 *	<li>WAITINGROOM : 8, 待接房</li>
 *</ul>
 *
 */
public enum AddressMappingStatus {
	ERRORSTATUS((byte)-1, "无效状态"),
	DEFAULT((byte)0, "其他"),
	LIVING((byte)1, "自用"), 
	RENT((byte)2, "出租"),
	FREE((byte)3, "待租"),
	SALED((byte)4, "已售"),
	UNSALE((byte)5, "待售"),
    OCCUPIED((byte)6, "已预订"),
	SIGNEDUP((byte)7, "待签约"),
    WAITINGROOM((byte)8, "待接房");
    
    private byte code;
    private String desc;
    
    private AddressMappingStatus(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getDesc() {
		return this.desc;
	}

	public static AddressMappingStatus fromCode(Byte code) {
       if (code != null) {
    	   for (AddressMappingStatus status : AddressMappingStatus.values()) {
			if (status.code == code.byteValue()) {
				return status;
			}
		}
       }
       return null;
    }
    
    public static AddressMappingStatus fromDesc(String desc) {
    	if (desc != null && !desc.isEmpty()) {
			for (AddressMappingStatus status : AddressMappingStatus.values()) {
				if (status.desc.equals(desc)) {
					return status;
				}
			}
		}
    	return AddressMappingStatus.ERRORSTATUS;
    }
}