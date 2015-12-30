package com.everhomes.rest.family;

import javax.validation.constraints.NotNull;


/**
 * <ul>
 * <li>type: 类型，小区或家庭{@link com.everhomes.rest.family.ParamType}</li>
 * <li>id: 类型对应的Id（小区id或家庭id）</li>
 * </ul>
 */
public class BaseCommand {
    
    private Byte type;
    @NotNull
    private Long id;
    
    public Byte getType() {
        return type;
    }
    public void setType(Byte type) {
        this.type = type;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

}
