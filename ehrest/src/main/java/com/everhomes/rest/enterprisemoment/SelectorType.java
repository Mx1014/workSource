package com.everhomes.rest.enterprisemoment;

/**
 * <ul>
 * <li>ALL(0): 全部</li>
 * <li>PUBLISH_BY_SELF(-1): 我发布的</li>
 * <li>FAVOURITE_BY_SELF(-2): 我点赞的</li>
 * <li>COMMENT_BY_SELF(-3): 我评论的</li>
 * <li>CUSTOM_TAG(-4): 用户tag</li>
 * </ul>
 */
public enum SelectorType {
    ALL(0), PUBLISH_BY_SELF(-1), FAVOURITE_BY_SELF(-2), COMMENT_BY_SELF(-3), CUSTOM_TAG(-4);

    private long code;

    SelectorType(long code) {
        this.code = code;
    }

    public long getCode() {
        return this.code;
    }

    public static SelectorType fromCode(Long code) {
        if (code == null) {
            return ALL;
        }
        for (SelectorType type : SelectorType.values()) {
            if (type.code == code.longValue()) {
                return type;
            }
        }
        return CUSTOM_TAG;
    }
}
