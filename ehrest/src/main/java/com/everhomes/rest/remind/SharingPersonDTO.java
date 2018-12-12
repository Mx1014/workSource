package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户ID</li>
 * <li>contractName: 共享人姓名</li>
 * </ul>
 */
public class SharingPersonDTO {
    private Long userId;
    private String contractName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
	        System.out.println("true");
	        return true;
	    }
	    if ((obj == null) || (obj.getClass() != this.getClass())) {
	        System.out.println("false");
	        return false;
	    }
	    if(this.userId == null){
	    	return false;
	    }
	    if (this.userId.equals(((SharingPersonDTO) obj).getUserId())) {
	        return true;
	    } else {
	        return false;
	    }
    }

	@Override
	public int hashCode() {
	    return userId==null ? 0 : userId.hashCode();
	} 
}
