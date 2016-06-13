package com.everhomes.rest.enterprise;

/**
 * <ul>创建通讯录实体。也就是具体的电话信息。
 * <li>contactId: 企业通讯录ID</li>
 * <li>entryType: 通讯录实体类型， 0表示手机号，1表示邮箱</li>
 * <li>entryValue: 实体具体值</li>
 * </ul>
 * @author janson
 *
 */
public class CreateContactEntryCommand {
    private java.lang.Long     contactId;
    private java.lang.Byte     entryType;
    private java.lang.String   entryValue;
    public java.lang.Long getContactId() {
        return contactId;
    }
    public void setContactId(java.lang.Long contactId) {
        this.contactId = contactId;
    }
    public java.lang.Byte getEntryType() {
        return entryType;
    }
    public void setEntryType(java.lang.Byte entryType) {
        this.entryType = entryType;
    }
    public java.lang.String getEntryValue() {
        return entryValue;
    }
    public void setEntryValue(java.lang.String entryValue) {
        this.entryValue = entryValue;
    }
    
    
}
