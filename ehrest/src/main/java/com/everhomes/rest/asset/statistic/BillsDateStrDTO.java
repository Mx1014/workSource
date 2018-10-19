//@formatter:off
package com.everhomes.rest.asset.statistic;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerId: 园区ID</li>
 * <li>ownerType: 园区类型</li>
 * <li>dateStr: 账期，格式为2018-06</li>
 *</ul>
 */
public class BillsDateStrDTO implements Cloneable{
	private Integer namespaceId;
	private Long ownerId;
	private String ownerType;
	private String dateStr;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillsDateStrDTO)) return false;

        BillsDateStrDTO that = (BillsDateStrDTO) o;

        if (getNamespaceId() != null ? !getNamespaceId().equals(that.getNamespaceId()) : that.getNamespaceId() != null)
            return false;
        if (getOwnerId() != null ? !getOwnerId().equals(that.getOwnerId()) : that.getOwnerId() != null)
            return false;
        if (getOwnerType() != null ? !getOwnerType().equals(that.getOwnerType()) : that.getOwnerType() != null)
            return false;
        return getDateStr() != null ? getDateStr().equals(that.getDateStr()) : that.getDateStr() == null;
    }

    @Override
    public int hashCode() {
        int result = getNamespaceId() != null ? getNamespaceId().hashCode() : 0;
        result = 31 * result + (getOwnerId() != null ? getOwnerId().hashCode() : 0);
        result = 31 * result + (getOwnerType() != null ? getOwnerType().hashCode() : 0);
        result = 31 * result + (getDateStr() != null ? getDateStr().hashCode() : 0);
        return result;
    }
    
    public Object clone() throws CloneNotSupportedException{
    	return super.clone();
    }
}
