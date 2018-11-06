package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>status: 状态</li>
 * <li>id: 主键</li>
 * </ul>
 */
public class UpdateButtScriptConfingStatusCommand {
    private Byte status;
    private Long  id ;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
