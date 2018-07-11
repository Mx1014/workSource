//@formatter:off
package com.everhomes.rest.asset;
/**
 * @author created by ycx
 * @date 下午2:34:30
 */

/**
 *<ul>
 * <li>defaultStatus: 1：代表使用的是默认配置，0：代表有做过个性化的修改</li>
 *</ul>
 */
public class IsProjectNavigateDefaultResp {
    private Byte defaultStatus;

	public Byte getDefaultStatus() {
		return defaultStatus;
	}

	public void setDefaultStatus(Byte defaultStatus) {
		this.defaultStatus = defaultStatus;
	}
	
}
