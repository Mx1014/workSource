// @formatter:off
package com.everhomes.rest.relocation;

/**
 * <p>搬迁节点参数</p>
 * <ul>
 * <li>APPROVED("APPROVED"): 通过审核</li>
 * </ul>
 */
public enum RelocationFlowNodeParam {
    APPROVED("APPROVED");

    private String code;
    private RelocationFlowNodeParam(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RelocationFlowNodeParam fromCode(String code) {
        if(code != null) {
            RelocationFlowNodeParam[] values = RelocationFlowNodeParam.values();
            for(RelocationFlowNodeParam value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
