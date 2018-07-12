// @formatter:off
package com.everhomes.rest.address;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 拆分/合并计划id</li>
 * <li>namespaceId: 域空间id</li>
 * <li>operationType: 操作类型：拆分（0），合并（1）</li>
 * <li>dateBegin: 拆分/合并计划的生效日期</li>
 * <li>addressId: 要执行拆分/合并计划的房源id</li>
 * <li>apartments: 拆分后、或者待合并的房源信息，参考{@link com.everhomes.rest.address.ArrangementApartmentDTO}</li>
 * </ul>
 */
public class AddressArrangementDTO {
	
	private Long id;
	private Integer namespaceId;
	private Byte operationType;
	private Long dateBegin;
	private Long addressId;
	@ItemType(ArrangementApartmentDTO.class)
    private List<ArrangementApartmentDTO> apartments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Byte getOperationType() {
		return operationType;
	}
	public void setOperationType(Byte operationType) {
		this.operationType = operationType;
	}
	public Long getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Long dateBegin) {
		this.dateBegin = dateBegin;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public List<ArrangementApartmentDTO> getApartments() {
		return apartments;
	}
	public void setApartments(List<ArrangementApartmentDTO> apartments) {
		this.apartments = apartments;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
