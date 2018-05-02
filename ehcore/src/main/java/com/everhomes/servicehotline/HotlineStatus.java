package com.everhomes.servicehotline;


/**
* <ul>客服/热线状态
* <li>DELETED: 0 已删除</li>
* <li>ACTIVE: 1 正常</li>
* </ul>
*/
public enum HotlineStatus {
	DELETED((byte)0),  ACTIVE((byte)1);
	private byte code;
	private HotlineStatus(byte code) {
		this.code = code;
	}
	public byte getCode(){
		return this.code;
	}
	public static HotlineStatus fromCode(Byte code){
		if (code != null) {
			for (HotlineStatus status : HotlineStatus.values()) {
				if (code.byteValue() == status.getCode()) {
					return status;
				}
			}
		}
		
		return null;
	}
}
