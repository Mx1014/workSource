// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;
import com.everhomes.rest.aclink.UpdateFirmwareDTO;
import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id：设备类型id</li>
 * <li>firmwareName：固件名称</li>
 * <li>firmwareId：固件id</li>
 * </ul>
 */
public class ChangeUpdateFirmwareCommand {

//    private Long id;
//    private String firmware;
//    private Long firmwareId;

    @ItemType(UpdateFirmwareDTO.class)
    private List <UpdateFirmwareDTO> dto;

    public List<UpdateFirmwareDTO> getDto() {
        return dto;
    }

    public void setDto(List<UpdateFirmwareDTO> dto) {
        this.dto = dto;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFirmware() {
//        return firmware;
//    }
//
//    public void setFirmware(String firmware) {
//        this.firmware = firmware;
//    }
//
//    public Long getFirmwareId() {
//        return firmwareId;
//    }
//
//    public void setFirmwareId(Long firmwareId) {
//        this.firmwareId = firmwareId;
//    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
