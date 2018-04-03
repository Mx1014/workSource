// @formatter:off
package com.everhomes.rest.flow;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>body: json字符串 {@link com.everhomes.rest.flow.CreateFlowGraphCommand}</li>
 * </ul>
 */
public class CreateFlowGraphJsonCommand {

    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
