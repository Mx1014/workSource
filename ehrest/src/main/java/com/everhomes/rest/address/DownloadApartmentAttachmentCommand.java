package com.everhomes.rest.address;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>attachmentId: 附件id</li>
 *     <li>addressId: 门牌id</li>
 * </ul>
 * Created by ying.xiong on 2017/11/28.
 */
public class DownloadApartmentAttachmentCommand {
    private Long attachmentId;

    private Long addressId;

    public Long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Long attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
