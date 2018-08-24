// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>navigatorAllIconUri: 容器组件“全部”按钮的IconUri</li>
 * </ul>
 */
public class UpdateLaunchPadConfigCommand {

    private Long id;
    private String navigatorAllIconUri;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNavigatorAllIconUri() {
        return navigatorAllIconUri;
    }

    public void setNavigatorAllIconUri(String navigatorAllIconUri) {
        this.navigatorAllIconUri = navigatorAllIconUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
