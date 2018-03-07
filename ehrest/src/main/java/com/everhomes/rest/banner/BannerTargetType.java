// @formatter:off
package com.everhomes.rest.banner;

/**
 * <ul>
 *     <li>NONE("NODE"): 不跳转</li>
 *     <li>POST_DETAIL("POST_DETAIL"): 帖子详情 {@link com.everhomes.rest.banner.targetdata.BannerPostTargetData}</li>
 *     <li>ACTIVITY_DETAIL("ACTIVITY_DETAIL"): 活动详情 {@link com.everhomes.rest.banner.targetdata.BannerActivityTargetData}</li>
 *     <li>APP("APP"): 应用 {@link com.everhomes.rest.banner.targetdata.BannerAppTargetData}</li>
 *     <li>URL("URL"): URL {@link com.everhomes.rest.banner.targetdata.BannerURLTargetData}</li>
 *     <li>ROUTE("ROUTE"): 路由 {@link com.everhomes.rest.banner.targetdata.BannerRouteTargetData}</li>
 * </ul>
 */
public enum BannerTargetType {

    NONE("NONE"),
    POST_DETAIL("POST_DETAIL"),
    ACTIVITY_DETAIL("ACTIVITY_DETAIL"),
    APP("APP"),
    URL("URL"),
    ROUTE("ROUTE"),;

    private String code;

    BannerTargetType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BannerTargetType fromCode(String code) {
        if (code != null) {
            for (BannerTargetType type : BannerTargetType.values()) {
                if (type.code.equals(code)) {
                    return type;
                }
            }
        }
        return null;
    }
}
