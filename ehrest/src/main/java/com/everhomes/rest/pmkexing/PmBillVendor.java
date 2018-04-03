package com.everhomes.rest.pmkexing;

/**
 * <ul>
 *     <li>ZZH_YI_BEI(999983, "yibei"): 一碑(正中会)</li>
 * </ul>
 */
public enum PmBillVendor {

    ZZH_YI_BEI(999983, "yibei");

    private Integer namespaceId;
    private String code;

    PmBillVendor(Integer namespaceId, String code) {
        this.namespaceId = namespaceId;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public static PmBillVendor fromCode(String code) {
        if (code != null) {
            for (PmBillVendor vendor : PmBillVendor.values()) {
                if (vendor.getCode().equals(code)) {
                    return vendor;
                }
            }
        }
        return null;
    }

    public static PmBillVendor fromNamespaceId(Integer namespaceId) {
        if (namespaceId != null) {
            for (PmBillVendor vendor : PmBillVendor.values()) {
                if (vendor.getNamespaceId().equals(namespaceId)) {
                    return vendor;
                }
            }
        }
        return null;
    }

    public static PmBillVendor fromNamespaceIdAndCode(Integer namespaceId, String code) {
        if (code != null && namespaceId != null) {
            for (PmBillVendor vendor : PmBillVendor.values()) {
                if (vendor.getNamespaceId().equals(namespaceId) && vendor.getCode().equals(code)) {
                    return vendor;
                }
            }
        }
        return null;
    }
}
