// @formatter:off
package com.everhomes.rest.payment;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>HANDLING("00"): 处理中</li>
 * <li>SUCCESS("01"): 成功</li>
 * <li>FAIL("02"): 失败</li>
 * <li>REVOKED("03"): 已撤销</li>
 * <li>REFUNDED("05"): 已退款</li>
 * <li>CHONGZHENG("04"): 已冲正</li>
 * <li>BILLED("06"): 已调账</li>
 * <li>PARTREFUNDED("07"): 已部分退货</li>
 * </ul>
 */
public enum TAOTAOGUCardTranscationStatus {
	HANDLING("00"), FAIL("02"),SUCCESS("01"),REVOKED("03"),
	REFUNDED("05"),CHONGZHENG("04"),BILLED("06"),PARTREFUNDED("07");
    
    private String code;
    private TAOTAOGUCardTranscationStatus(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static TAOTAOGUCardTranscationStatus fromCode(String code) {
        if(code != null) {
            TAOTAOGUCardTranscationStatus[] values = TAOTAOGUCardTranscationStatus.values();
            for(TAOTAOGUCardTranscationStatus value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
