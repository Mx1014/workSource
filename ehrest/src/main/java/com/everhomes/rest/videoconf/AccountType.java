package com.everhomes.rest.videoconf;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ACCOUNT_TYPE_SINGLE: 单账号</li>
 * <li>ACCOUNT_TYPE_MULTIPLE: 多账号</li>
 * </ul>
 */
public enum AccountType {

	ACCOUNT_TYPE_SINGLE("单账号"), ACCOUNT_TYPE_MULTIPLE("多账号");
    
    private String code;
    private AccountType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }

	public static AccountType fromCode(String code) {
		for(AccountType v :AccountType.values()){
			if(v.getCode().equals(code))
				return v;
		}
		return null;
	}

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
