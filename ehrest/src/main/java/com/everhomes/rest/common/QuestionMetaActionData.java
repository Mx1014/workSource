// @formatter:off
package com.everhomes.rest.common;

import com.everhomes.rest.messaging.QuestionMetaObject;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>metaObject: metaObject</li>
 * </ul>
 */
public class QuestionMetaActionData {

    private QuestionMetaObject metaObject;

    public QuestionMetaObject getMetaObject() {
        return metaObject;
    }

    public void setMetaObject(QuestionMetaObject metaObject) {
        this.metaObject = metaObject;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
