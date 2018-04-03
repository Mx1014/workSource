// @formatter:off
package com.everhomes.rest.launchad;

/**
 * <ul>
 *     <li>IMAGE("IMAGE"): 图片</li>
 *     <li>VIDEO("VIDEO"): 视频</li>
 * </ul>
 */
public enum LaunchAdType {

    IMAGE("IMAGE"), VIDEO("VIDEO");

    private String code;

    LaunchAdType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static LaunchAdType fromCode(String code) {
        for (LaunchAdType launchAdType : LaunchAdType.values()) {
            if (launchAdType.getCode().equals(code)) {
                return launchAdType;
            }
        }
        return null;
    }
}
