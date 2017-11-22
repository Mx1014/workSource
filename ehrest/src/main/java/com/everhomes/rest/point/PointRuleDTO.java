package com.everhomes.rest.point;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>systemId: systemId</li>
 *     <li>moduleId: moduleId</li>
 *     <li>displayName: displayName</li>
 *     <li>operateType: operateType</li>
 *     <li>points: points</li>
 *     <li>limitType: limitType</li>
 *     <li>limitData: limitData</li>
 *     <li>eventName: eventName</li>
 *     <li>status: status</li>
 *     <li>createTime: createTime</li>
 *     <li>updateTime: updateTime</li>
 * </ul>
 */
public class PointRuleDTO {
    private Long id;
    private Integer namespaceId;
    private Long systemId;
    private Long moduleId;
    private String displayName;
    private Byte operateType;
    private Integer points;
    private Byte limitType;
    private String limitData;
    private String eventName;
    private Byte status;
    private Timestamp createTime;
    private Timestamp updateTime;
}
