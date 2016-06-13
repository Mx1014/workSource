// @formatter:off
package com.everhomes.rest.ui.user;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sceneToken: 场景标识，用一个标识代替原来用多个字段共同表示的标识，以使传参数简单一些（只需要传一个参数）</li>
 * <li>pageAnchor:锚点</li>
 * <li>pageSize:每页的个数</li>
 * </ul>
 */
public class GetUserOpPromotionCommand {
    private String sceneToken;
    private Long pageAnchor;
    private Integer pageSize;

    public GetUserOpPromotionCommand() {
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
