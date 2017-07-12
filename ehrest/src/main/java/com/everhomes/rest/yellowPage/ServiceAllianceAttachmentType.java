package com.everhomes.rest.yellowPage;

/**
 * <ul>
 *     <li>BANNER((byte)0): 服务联盟企业banners</li>
 *     <li>FILE_ATTACHMENT((byte)1): 附件</li>
 * </ul>
 */
public enum ServiceAllianceAttachmentType {
    BANNER((byte)0), FILE_ATTACHMENT((byte)1);

    private byte code;
    private ServiceAllianceAttachmentType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return this.code;
    }

    public static ServiceAllianceAttachmentType fromCode(byte code) {
        for(ServiceAllianceAttachmentType t : ServiceAllianceAttachmentType.values()) {
            if (t.code == code) {
                return t;
            }
        }

        return null;
    }
}
