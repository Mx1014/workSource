package com.everhomes.rest.general_approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>unAuthFlag:未认证用户是否可填写: 1-是 0-否 </li>
 * </ul>
 * @author janson
 *
 */
public class GeneralFormContactDTO {
	
	private Byte unAuthFlag;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }



	public Byte getUnAuthFlag() {
		return unAuthFlag;
	}


	public void setUnAuthFlag(Byte unAuthFlag) {
		this.unAuthFlag = unAuthFlag;
	}



 
}
