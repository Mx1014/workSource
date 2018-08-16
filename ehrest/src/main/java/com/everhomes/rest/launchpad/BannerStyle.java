// @formatter:off
package com.everhomes.rest.launchpad;

/**
 * <ul>
 *     <li>DEFAULT("Default"): 默认</li>
 *     <li>SHAPE("Shape"): 滑动变形样式</li>
 * </ul>
 */
public enum BannerStyle {
    DEFAULT("Default"), SHAPE("Shape");

    private String code;

    private BannerStyle(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static BannerStyle fromCode(String code) {
        BannerStyle[] values = BannerStyle.values();
        for (BannerStyle value : values) {
            if (value.code.equals(code)) {
                return value;
            }
        }

        return null;
    }
}
