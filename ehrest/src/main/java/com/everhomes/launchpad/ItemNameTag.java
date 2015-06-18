// @formatter:off
package com.everhomes.launchpad;

/**
 * <p>服务市场条目标签</p>
 * <ul>
 * <li>BIZ: 商家</li>
 * <li>PM: 物业</li>
 * <li>GARC: 业委，Government Agency - Resident Committee</li>
 * <li>GANC: 居委，Government Agency - Neighbor Committee</li>
 * <li>GAPS: 公安，Government Agency - Police Station</li>
 * <li>TP: 第三方服务，THIRD PART</li>
 * </ul>
 */
public enum ItemNameTag {
    BIZ("BIZ"), PM("PM"), GARC("GARC"), GANC("GANC"), GAPS("GAPS"), TP("TP");
    
    private String code;
    private ItemNameTag(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ItemNameTag fromCode(String code) {
        ItemNameTag[] values = ItemNameTag.values();
        for(ItemNameTag value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
