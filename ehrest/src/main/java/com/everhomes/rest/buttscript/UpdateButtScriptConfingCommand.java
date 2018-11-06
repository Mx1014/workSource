package com.everhomes.rest.buttscript;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>infoDescribe: 描述信息</li>
 * <li>remark: 备注</li>
 * <li>id: 主键</li>
 * </ul>
 */
public class UpdateButtScriptConfingCommand {
    private String  infoDescribe;
    private String remark ;
    private Long  id ;

    public String getInfoDescribe() {
        return infoDescribe;
    }

    public void setInfoDescribe(String infoDescribe) {
        this.infoDescribe = infoDescribe;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
