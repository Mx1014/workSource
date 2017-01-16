package com.everhomes.rest.energy;

/**
 * <ul>
 *     <li>RECEIVABLE: 1 应收</li>
 *     <li>PAYABLE : 2 应付</li>
 * </ul>
 */
public enum EnergyCategoryDefault {

	RECEIVABLE(1L,"应收"), PAYABLE(2L,"应付");

    private long code;
    private String name;

    EnergyCategoryDefault(long code,String name) {
        this.code = code;
        this.name = name;
    }

    public long getCode() {
        return code;
    }
    
    

    public static EnergyCategoryDefault fromCode(Long code) {
        for (EnergyCategoryDefault type : EnergyCategoryDefault.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

	public String getName() {
		return name;
	}
 
}
