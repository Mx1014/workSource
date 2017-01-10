package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>createTime：时间</li>
 * <li>createUid：操作人用户id</li>
 * <li>createUname：操作人姓名</li>
 * <li>createUtoken：操作人手机号</li>
 * <li>description：操作描述</li>
 * </ul>
 */
public class DoorAuthLogDTO {

    private String description;

    private Long createTime;

    private String createUname;

    private String createUtoken;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUname() {
        return createUname;
    }

    public void setCreateUname(String createUname) {
        this.createUname = createUname;
    }

    public String getCreateUtoken() {
        return createUtoken;
    }

    public void setCreateUtoken(String createUtoken) {
        this.createUtoken = createUtoken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
