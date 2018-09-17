// @formatter:off
package com.everhomes.community_form;

import com.everhomes.server.schema.tables.pojos.EhCommunityGeneralForm;
import com.everhomes.util.StringHelper;

public class CommunityGeneralForm extends EhCommunityGeneralForm{
    private static final long serialVersionUID = -6217330260393012217L;
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
