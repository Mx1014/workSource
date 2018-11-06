package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>useTotal：功能使用合计</li>
 * <li>ActiveDoorByPlaceDTO: 已激活门禁数（按门禁位置），参考{@link com.everhomes.rest.aclink.ActiveDoorByPlaceDTO}</li>
 * <li>ActiveDoorByFirmwareDTO: 已激活门禁数（按固件版本），参考{@link com.everhomes.rest.aclink.ActiveDoorByFirmwareDTO}</li>
 * <li>ActiveDoorByEquipmentDTO: 已激活门禁数（按设备类型），参考{@link com.everhomes.rest.aclink.ActiveDoorByEquipmentDTO}</li>
 * <li>ActiveDoorByNamespaceDTO: 已激活门禁数（按域空间统计），参考{@link com.everhomes.rest.aclink.ActiveDoorByNamespaceDTO}</li>
 * <li>AclinkUseByNamespaceDTO: 功能使用（按域空间统计）参考{@link com.everhomes.rest.aclink.AclinkUseByNamespaceDTO}</li>
 * </ul>
 */

public class DoorStatisticEhResponse {
    private Long useTotal;
    @ItemType(ActiveDoorByPlaceDTO.class)
    private List<ActiveDoorByPlaceDTO> dto1;
    @ItemType(ActiveDoorByFirmwareDTO.class)
    private List<ActiveDoorByFirmwareDTO> dto2;
    @ItemType(ActiveDoorByEquipmentDTO.class)
    private List<ActiveDoorByEquipmentDTO> dto3;
    @ItemType(ActiveDoorByNamespaceDTO.class)
    private List<ActiveDoorByNamespaceDTO> dto4;
    @ItemType(AclinkUseByNamespaceDTO.class)
    private List<AclinkUseByNamespaceDTO> dto5;
    public List<ActiveDoorByPlaceDTO> getDto1() {
        return dto1;
    }

    public void setDto1(List<ActiveDoorByPlaceDTO> dto1) {
        this.dto1 = dto1;
    }

    public List<ActiveDoorByFirmwareDTO> getDto2() {
        return dto2;
    }

    public void setDto2(List<ActiveDoorByFirmwareDTO> dto2) {
        this.dto2 = dto2;
    }

    public List<ActiveDoorByEquipmentDTO> getDto3() {
        return dto3;
    }

    public void setDto3(List<ActiveDoorByEquipmentDTO> dto3) {
        this.dto3 = dto3;
    }

    public List<ActiveDoorByNamespaceDTO> getDto4() {
        return dto4;
    }

    public void setDto4(List<ActiveDoorByNamespaceDTO> dto4) {
        this.dto4 = dto4;
    }

    public Long getUseTotal() {
        return useTotal;
    }

    public void setUseTotal(Long useTotal) {
        this.useTotal = useTotal;
    }

    public List<AclinkUseByNamespaceDTO> getDto5() {
        return dto5;
    }

    public void setDto5(List<AclinkUseByNamespaceDTO> dto5) {
        this.dto5 = dto5;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}

