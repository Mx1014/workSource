package com.everhomes.rest.organization.pm;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId : 家庭id	</li>
 *</ul>
 *
 */
public class GetFamilyStatisticCommand {
	@NotNull
	private Long familyId;

	public Long getFamilyId() {
		return familyId;
	}

	public void setFamilyId(Long familyId) {
		this.familyId = familyId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
