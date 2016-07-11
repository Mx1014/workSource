// @formatter:off
package com.everhomes.category;

import com.everhomes.server.schema.tables.pojos.EhCategories;
import com.everhomes.util.StringHelper;

public class Category extends EhCategories {
    private static final long serialVersionUID = 757891813552223059L;
    
    public Category() {
    }
    
    public Category(Long id, Long parentId, String name, String path, Byte status) {
        this.setId(id);
        this.setParentId(parentId);
        this.setName(name);
        this.setPath(path);
        this.setStatus(status);
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
