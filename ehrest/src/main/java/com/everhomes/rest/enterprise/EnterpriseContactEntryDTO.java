package com.everhomes.rest.enterprise;

import java.sql.Timestamp;

/**
 * <ul>
 * 通讯录实体
 * </ul>
 * @author janson
 *
 */
public class EnterpriseContactEntryDTO {
    private Long id;
    private Long     contactId;
    private Byte     entryType;
    private String   entryValue;
    private Long     creatorUid;
    private Timestamp createTime;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getContactId() {
        return contactId;
    }
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
    public Byte getEntryType() {
        return entryType;
    }
    public void setEntryType(Byte entryType) {
        this.entryType = entryType;
    }
    public String getEntryValue() {
        return entryValue;
    }
    public void setEntryValue(String entryValue) {
        this.entryValue = entryValue;
    }
    public Long getCreatorUid() {
        return creatorUid;
    }
    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }
    public Timestamp getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    
}
