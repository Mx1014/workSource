// @formatter:off
package com.everhomes.rest.launchpadbase;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: Id</li>
 *     <li>type: 主页签类型，参考{@link IndexType}</li>
 *     <li>name: 名字</li>
 *     <li>iconUrl: Icon</li>
 *     <li>selectIconUrl: 被选中后的Icon</li>
 *     <li>configJson: 主页签内部配置信息，根据type名字到com.everhomes.rest.launchpadbase.Indexconfigjson获取对象，比如“工作台”的layoutId，“我”里面是否有“我的发布”</li>
 * </ul>
 */
public class IndexDTO {

    private Long id;

    private Byte type;

    private String name;

    private String iconUrl;

    private String selectIconUrl;

    private String configJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSelectIconUrl() {
        return selectIconUrl;
    }

    public void setSelectIconUrl(String selectIconUrl) {
        this.selectIconUrl = selectIconUrl;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
