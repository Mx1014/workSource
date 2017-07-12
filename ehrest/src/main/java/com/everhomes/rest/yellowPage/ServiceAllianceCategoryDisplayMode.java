// @formatter:off
package com.everhomes.rest.yellowPage;

/**
 * <ul>
 *     <li>LIST(1)：列表</li>
 *     <li>IMAGE(2)：大图</li>
 *     <li>IMAGE_APPLY(3)：大图带申请</li>
 * </ul>
 */
public enum ServiceAllianceCategoryDisplayMode {
    LIST((byte)1), IMAGE((byte) 2), IMAGE_APPLY((byte)3);

    private Byte code;

    ServiceAllianceCategoryDisplayMode(Byte code) {
        this.code = code;
    }

    public static ServiceAllianceCategoryDisplayMode fromCode(Byte code) {
        for (ServiceAllianceCategoryDisplayMode category : ServiceAllianceCategoryDisplayMode.values()) {
            if (category.code.equals(code)) {
                return category;
            }
        }
        return null;
    }

    public Byte getCode() {
        return code;
    }
}
