package com.everhomes.forum;

import com.everhomes.namespace.Namespace;
import com.everhomes.server.schema.tables.pojos.EhForums;

/**
 * 
 * API level forum Object. It extends from persist layer object directly since they literally are the same
 * 
 * @author Kelven Yang
 *
 */
public class Forum extends EhForums {
    private static final long serialVersionUID = -2708437410506916026L;
    
    public Forum() {
        this.setNamespaceId(Namespace.DEFAULT_NAMESPACE);
    }
}
