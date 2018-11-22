package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>scenes: 场景列表 参考{@link com.everhomes.rest.enterprisepaymentauth.SceneSimpleDTO}</li>
 * </ul>
 */
public class ListPaymentScenesResponse {
    private List<SceneSimpleDTO> scenes;

    public List<SceneSimpleDTO> getScenes() {
        return scenes;
    }

    public void setScenes(List<SceneSimpleDTO> scenes) {
        this.scenes = scenes;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
