// @formatter:off
// generated file: DO NOT EDIT
package com.everhomes.rest.scene.admin;

import com.everhomes.rest.RestResponseBase;

import java.util.List;
import com.everhomes.rest.scene.SceneTypeInfoDTO;

public class SceneListSceneTypesRestResponse extends RestResponseBase {

    private List<SceneTypeInfoDTO> response;

    public SceneListSceneTypesRestResponse () {
    }

    public List<SceneTypeInfoDTO> getResponse() {
        return response;
    }

    public void setResponse(List<SceneTypeInfoDTO> response) {
        this.response = response;
    }
}
