package com.everhomes.rest.acl;

import javax.validation.constraints.NotNull;


/**
 * <ul>
 *     <li>id: id</li>
 * </ul>
 */
public class FindServiceModuleAppCommand {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
