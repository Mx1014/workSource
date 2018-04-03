package com.everhomes.rest.pmkexing;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.AddressDTO;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>organizationId: 公司id</li>
 *     <li>latestSelected: 最后一次选中标示 1: 选中, 0: 未选中</li>
 *     <li>organizationName: 公司名称</li>
 *     <li>areaSize: 公司地址总面积</li>
 *     <li>addresses: 公司地址信息 {@link com.everhomes.rest.address.AddressDTO}</li>
 * </ul>
 */
public class ListOrganizationsByPmAdminDTO {

    private Long organizationId;
    private Byte latestSelected = 0;
    private String organizationName;
    @ItemType(AddressDTO.class)
    private List<AddressDTO> addresses = new ArrayList<>();

    private Double areaSize;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public List<AddressDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    public Byte getLatestSelected() {
        return latestSelected;
    }

    public void setLatestSelected(Byte latestSelected) {
        this.latestSelected = latestSelected;
    }

    public Double getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(Double areaSize) {
        this.areaSize = areaSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
