package com.everhomes.rest.videoconf;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>confId: 会议id</li>
 * </ul>
 */
public class CancelVideoConfCommand {

	private Long confId;
	
	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
