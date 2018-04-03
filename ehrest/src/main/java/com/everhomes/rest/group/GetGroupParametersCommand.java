// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>clubType: clubType 参考{@link ClubType}</li>
 * </ul>
 */
public class GetGroupParametersCommand {

	private Integer namespaceId;

	private Byte clubType;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getClubType() {
		return clubType;
	}

	public void setClubType(Byte clubType) {
		this.clubType = clubType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
