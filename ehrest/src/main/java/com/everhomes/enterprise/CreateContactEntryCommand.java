package com.everhomes.enterprise;

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
}
