package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>levelId: 门禁授权层级对象的 ID </li>
 * <li>levelType: 门禁授权层级对象的类型 {@link com.everhomes.rest.aclink.DoorAccessOwnerType} </li>
 * <li>rightVisitor: 访客授权，1 表示授权，0 表示非授权</li>
 * <li>rightOpen: 开门权限，1 表示授权，0 表示非授权</li>
 * <li>rightRemote: 远程开门授权，1 表示授权，0 表示非授权</li>
 * <li>doorId: 门禁 ID</li>
 * </ul>
 * @author janson
 *
 */
public class GetDoorAuthLevelCommand {
    @NotNull
    private Long     levelId;
    
    @NotNull
    private Byte     levelType;
    
    @NotNull
    private Long     doorId;

    public Long getLevelId() {
        return levelId;
    }

    public void setLevelId(Long levelId) {
        this.levelId = levelId;
    }

    public Byte getLevelType() {
        return levelType;
    }

    public void setLevelType(Byte levelType) {
        this.levelType = levelType;
    }

    public Long getDoorId() {
        return doorId;
    }

    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
