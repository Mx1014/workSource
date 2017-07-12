package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>siteNumber: 资源编号</li>
 * <li>siteNumberGroup: 关联性分组(同一个分组号码的会成为一组)</li> 
 * <li>groupLockFlag: 一个资源被预约是否锁整个group,0-否,1-是</li> 
 * </ul>
 */
public class SiteNumberDTO {

	private String siteNumber ;
	private Integer siteNumberGroup;
    private Byte groupLockFlag;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getSiteNumber() {
		return siteNumber;
	}
	public void setSiteNumber(String siteNumber) {
		this.siteNumber = siteNumber;
	}
	public Integer getSiteNumberGroup() {
		return siteNumberGroup;
	}
	public void setSiteNumberGroup(Integer siteNumberGroup) {
		this.siteNumberGroup = siteNumberGroup;
	}
	public Byte getGroupLockFlag() {
		return groupLockFlag;
	}
	public void setGroupLockFlag(Byte groupLockFlag) {
		this.groupLockFlag = groupLockFlag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SiteNumberDTO that = (SiteNumberDTO) o;

		return siteNumber != null ? siteNumber.equals(that.siteNumber) : that.siteNumber == null;
	}

	@Override
	public int hashCode() {
		return siteNumber != null ? siteNumber.hashCode() : 0;
	}
}
