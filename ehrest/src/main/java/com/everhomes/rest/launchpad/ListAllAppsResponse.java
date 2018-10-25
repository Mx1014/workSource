package com.everhomes.rest.launchpad;

import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>defaultDtos: 首页的Items信息（默认、自定义），参考{@link AppDTO}</li>
 *     <li>categoryDtos: “全部”-各个分类的item信息汇总。“更多”-所有的信息，List的size为1，里面包括defaultDtos。 参考{@link LaunchPadCategoryDTO}</li>
 * </ul>
 */
public class ListAllAppsResponse {

    private List<AppDTO> defaultDtos;

    private List<LaunchPadCategoryDTO> categoryDtos;

    public List<AppDTO> getDefaultDtos() {
        return defaultDtos;
    }

    public void setDefaultDtos(List<AppDTO> defaultDtos) {
        this.defaultDtos = defaultDtos;
    }

    public List<LaunchPadCategoryDTO> getCategoryDtos() {
        return categoryDtos;
    }

    public void setCategoryDtos(List<LaunchPadCategoryDTO> categoryDtos) {
        this.categoryDtos = categoryDtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
