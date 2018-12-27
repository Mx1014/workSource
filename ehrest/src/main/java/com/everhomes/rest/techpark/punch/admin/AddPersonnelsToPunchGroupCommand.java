package com.everhomes.rest.techpark.punch.admin;

import java.util.List;

import com.everhomes.rest.uniongroup.UniongroupTarget;

/**
 * <ul> 
 * <li>punchRuleId：考勤组的id</li>
 * <li>targets：考勤组新增的关联对象</li> 
 * </ul>
 */

public class AddPersonnelsToPunchGroupCommand {
	private Long punchRuleId;
	private List<UniongroupTarget> targets;

	public Long getPunchRuleId() {
		return punchRuleId;
	}

	public void setPunchRuleId(Long punchRuleId) {
		this.punchRuleId = punchRuleId;
	}

	public List<UniongroupTarget> getTargets() {
		return targets;
	}

	public void setTargets(List<UniongroupTarget> targets) {
		this.targets = targets;
	}
}
