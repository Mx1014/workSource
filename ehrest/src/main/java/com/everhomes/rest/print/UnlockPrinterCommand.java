// @formatter:off
package com.everhomes.rest.print;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>ownerType : 打印所属类型，此处为community, 参考{@link com.everhomes.rest.print.PrintOwnerType}</li>
 * <li>ownerId : 所属id</li>
 * <li>readerName : 刷卡器(已废弃)</li>
 * <li>privileges : 权限列表，参考 {@link com.everhomes.rest.print.PrintJobTypeType} （已废弃）</li>
 * <li>qrid : 二维码信息</li>
 * </ul>
 *
 *  @author:dengs 2017年6月20日
 */
public class UnlockPrinterCommand {
	private String ownerType;
	private Long ownerId;
	@Deprecated //废弃参数
	private String readerName;
	
	private String qrid;
	
	@Deprecated
	@ItemType(Byte.class)
	private List<Byte> privileges;
	private Integer namespaceId;

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public List<Byte> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<Byte> privileges) {
		this.privileges = privileges;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getReaderName() {
		return readerName;
	}

	public void setReaderName(String readerName) {
		this.readerName = readerName;
	}
	
	public String getQrid() {
		return qrid;
	}

	public void setQrid(String qrid) {
		this.qrid = qrid;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
