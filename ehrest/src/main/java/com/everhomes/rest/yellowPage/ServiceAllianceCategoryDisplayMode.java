// @formatter:off
package com.everhomes.rest.yellowPage;

import java.util.Objects;

/**
 * <ul>
 *     <li>LIST(1)列表</li>
 *     <li>IMAGE(2)大图</li>
 * </ul>
 */
public enum ServiceAllianceCategoryDisplayMode {
    LIST((byte)1), IMAGE((byte) 2);

    private Byte code;

    ServiceAllianceCategoryDisplayMode(Byte code) {
        this.code = code;
    }

    public static ServiceAllianceCategoryDisplayMode fromCode(Byte code) {
        for (ServiceAllianceCategoryDisplayMode category : ServiceAllianceCategoryDisplayMode.values()) {
            if (Objects.equals(category.code, code)) {
                return category;
            }
        }
        return null;
    }

    public Byte getCode() {
        return code;
    }
}
