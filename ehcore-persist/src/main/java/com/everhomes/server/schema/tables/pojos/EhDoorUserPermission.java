package com.everhomes.server.schema.tables.pojos;

import java.io.Serializable;
import java.sql.Timestamp;

public class EhDoorUserPermission implements Serializable {
    private static final long serialVersionUID = 943608237L;
    private Long id;
    private Integer namespaceId;
    private Long userId;
    private Long approveUserId;
    private Byte authType;
    private Long integralTag1;
    private Long integralTag2;
    private Long integralTag3;
    private Long integralTag4;
    private String stringTag1;
    private String stringTag2;
    private String stringTag3;
    private String stringTag4;
    private String description;
    private Timestamp createTime;
    private Byte status;

    public EhDoorUserPermission() {
    }

    public EhDoorUserPermission(Long id, Integer namespaceId, Long userId, Long approveUserId, Byte authType,
            Long integralTag1, Long integralTag2, Long integralTag3, Long integralTag4, String stringTag1,
            String stringTag2, String stringTag3, String stringTag4, String description, Timestamp createTime,
            Byte status) {
        this.id = id;
        this.namespaceId = namespaceId;
        this.userId = userId;
        this.approveUserId = approveUserId;
        this.authType = authType;
        this.integralTag1 = integralTag1;
        this.integralTag2 = integralTag2;
        this.integralTag3 = integralTag3;
        this.integralTag4 = integralTag4;
        this.stringTag1 = stringTag1;
        this.stringTag2 = stringTag2;
        this.stringTag3 = stringTag3;
        this.stringTag4 = stringTag4;
        this.description = description;
        this.createTime = createTime;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return this.namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApproveUserId() {
        return this.approveUserId;
    }

    public void setApproveUserId(Long approveUserId) {
        this.approveUserId = approveUserId;
    }

    public Byte getAuthType() {
        return this.authType;
    }

    public void setAuthType(Byte authType) {
        this.authType = authType;
    }

    public Long getIntegralTag1() {
        return this.integralTag1;
    }

    public void setIntegralTag1(Long integralTag1) {
        this.integralTag1 = integralTag1;
    }

    public Long getIntegralTag2() {
        return this.integralTag2;
    }

    public void setIntegralTag2(Long integralTag2) {
        this.integralTag2 = integralTag2;
    }

    public Long getIntegralTag3() {
        return this.integralTag3;
    }

    public void setIntegralTag3(Long integralTag3) {
        this.integralTag3 = integralTag3;
    }

    public Long getIntegralTag4() {
        return this.integralTag4;
    }

    public void setIntegralTag4(Long integralTag4) {
        this.integralTag4 = integralTag4;
    }

    public String getStringTag1() {
        return this.stringTag1;
    }

    public void setStringTag1(String stringTag1) {
        this.stringTag1 = stringTag1;
    }

    public String getStringTag2() {
        return this.stringTag2;
    }

    public void setStringTag2(String stringTag2) {
        this.stringTag2 = stringTag2;
    }

    public String getStringTag3() {
        return this.stringTag3;
    }

    public void setStringTag3(String stringTag3) {
        this.stringTag3 = stringTag3;
    }

    public String getStringTag4() {
        return this.stringTag4;
    }

    public void setStringTag4(String stringTag4) {
        this.stringTag4 = stringTag4;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
