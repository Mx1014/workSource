package com.everhomes.videoconf;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>sourceAccountName: 源账号</li>
 *  <li>confId: 会议id</li>
 * </ul>
 */
public class CancelVideoConfCommand {

	private Long confId;
	
	private String sourceAccountName;
	
	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public String getSourceAccountName() {
		return sourceAccountName;
	}

	public void setSourceAccountName(String sourceAccountName) {
		this.sourceAccountName = sourceAccountName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
