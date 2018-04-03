package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul> 获取二维码信息列表
 * <li>keys: 门禁信息 {@link com.everhomes.rest.aclink.DoorAccessQRKeyDTO} </li>
 * <li>qrTimeout: 二维码超时时间 </li>
 * <li>qrIntro: 使用说明链接地址 </li>
 * </ul>
 * @author janson
 *
 */
public class ListDoorAccessQRKeyResponse {
    @ItemType(DoorAccessQRKeyDTO.class)
    List<DoorAccessQRKeyDTO> keys;
    
    Long qrTimeout;
    String qrIntro;

    public List<DoorAccessQRKeyDTO> getKeys() {
        return keys;
    }

    public void setKeys(List<DoorAccessQRKeyDTO> keys) {
        this.keys = keys;
    }

    public Long getQrTimeout() {
        return qrTimeout;
    }

    public void setQrTimeout(Long qrTimeout) {
        this.qrTimeout = qrTimeout;
    }

    public String getQrIntro() {
        return qrIntro;
    }

    public void setQrIntro(String qrIntro) {
        this.qrIntro = qrIntro;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
