package com.everhomes.rest.parking;

/**
 * <li>FORBIDDEN(0): 禁用</li>
 * <li>QUEQUE(1): 排队模式</li>
 * <li>SEMI_AUTOMATIC(2): 半自动化模式</li>
 * <li>INTELLIGENT(3): 智能模式</li>
 * </ul>
 */
public enum ParkingRequestFlowType {
	 FORBIDDEN(0), QUEQUE(1), SEMI_AUTOMATIC(2), INTELLIGENT(3);
	    
	 private Integer code;
	    
	 private ParkingRequestFlowType(Integer code) {
	        this.code = code;
	    }
	    
	    public Integer getCode() {
	        return this.code;
	    }
	    
	    public static ParkingRequestFlowType fromCode(Integer code) {
	        if(code != null) {
	        	ParkingRequestFlowType[] values = ParkingRequestFlowType.values();
	            for(ParkingRequestFlowType value : values) {
	                if(value.code == code.intValue()) {
	                    return value;
	                }
	            }
	        }
	        
	        return null;
	    }	    
}
