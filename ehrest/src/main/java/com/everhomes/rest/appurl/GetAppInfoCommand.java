package com.everhomes.rest.appurl;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>namespaceId: 域空间</li>
 *  <li>osType: 操作系统类型 参考{@link com.everhomes.rest.user.OSType}</li>
 * </ul>
 *
 */
public class GetAppInfoCommand {
	
	@NotNull
	private Integer namespaceId;
	@NotNull
	private Byte osType;
	
	public GetAppInfoCommand() {
		super();
	}

	public GetAppInfoCommand(Integer namespaceId, Byte osType) {
		super();
		this.namespaceId = namespaceId;
		this.osType = osType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getOsType() {
		return osType;
	}

	public void setOsType(Byte osType) {
		this.osType = osType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
