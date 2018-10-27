//@formatter:off
package com.everhomes.asset;

import com.everhomes.util.StringHelper;
import com.everhomes.server.schema.tables.pojos.EhAssetDooraccessLogs;
/**
 * Created by djm
 */

public class AssetDooraccessLog extends EhAssetDooraccessLogs {
    
	private static final long serialVersionUID = 7492685420500802590L;
	/**
	 * 
	 */
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
