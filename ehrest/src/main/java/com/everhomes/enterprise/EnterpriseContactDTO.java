package com.everhomes.enterprise;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>userId: 通讯录绑定的用户ID</li>
 * <li>role: TODO 用户在公司的角色</li>
 * <li>enterpriseId: 用户所在企业的ID</li>
 * </ul>
 * @author janson
 *
 */
public class EnterpriseContactDTO {
    private java.lang.Long     id;
    private java.lang.Long     enterpriseId;
    private java.lang.String   name;
    private java.lang.String   nickName;
    private java.lang.String   avatar;
    private java.lang.Long     userId;
    private java.lang.Long     role;
    private java.lang.Byte     status;
    private java.lang.Long     creatorUid;
    private java.sql.Timestamp createTime;
    
    @ItemType(EnterpriseContactEntryDTO.class)
    private List<EnterpriseContactEntryDTO> entries;
}
