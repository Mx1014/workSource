// @formatter:off
package com.everhomes.scene;

import com.everhomes.server.schema.tables.pojos.EhSceneTypes;
import com.everhomes.util.StringHelper;

public class SceneTypeInfo extends EhSceneTypes {
    private static final long serialVersionUID = 9108860081557991287L;

    public SceneTypeInfo() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
