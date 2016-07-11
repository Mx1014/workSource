package com.everhomes.border;

import com.everhomes.server.schema.tables.pojos.EhBorders;
import com.everhomes.util.StringHelper;

/**
 * Border server information record
 * 
 * @author Kelven Yang
 *
 */
public class Border extends EhBorders {
    private static final long serialVersionUID = 6903984675745529631L;

    public Border() {
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
