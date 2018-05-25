// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul> 查询转发的消息
 * <li>rightRemote: 是否有远程开门权限0无1有</li>
 * </ul>
 * @author janson
 *
 */
public class listAdminAesUserKeyCommand {
	private Byte rightRemote;

	public Byte getRightRemote() {
		return rightRemote;
	}

	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
