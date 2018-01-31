package com.everhomes.rest.contract;


/**
 * <ul>
 *     <li>id: id</li>
 *     <li>groupType: 参考{@link com.everhomes.rest.contract.ContractParamGroupType}</li>
 *     <li>paramId: 参数id</li>
 *     <li>groupId: 部门id</li>
 *     <li>positionId: 岗位id</li>
 * </ul>
 * Created by ying.xiong on 2018/1/4.
 */
public class ContractParamGroupMapDTO {
    private Long id;
    private Byte groupType;
    private Long paramId;
    private Long groupId;
    private Long positionId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGroupType() {
        return groupType;
    }

    public void setGroupType(Byte groupType) {
        this.groupType = groupType;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParamId() {
        return paramId;
    }

    public void setParamId(Long paramId) {
        this.paramId = paramId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }
}
