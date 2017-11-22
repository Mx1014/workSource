package com.everhomes.rest.point;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>displayName: displayName</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 * </ul>
 */
public class PointModuleDTO {
    private Long id;
    private Integer namespaceId;
    private String displayName;
    private Byte status;
    private Timestamp createTime;
}
