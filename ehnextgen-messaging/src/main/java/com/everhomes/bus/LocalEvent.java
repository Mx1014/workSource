package com.everhomes.bus;

import java.util.Map;

/**
 * <ul>
 *     <li>syncFlag: syncFlag</li>
 *     <li>eventName: eventName</li>
 *     <li>entityType: entityType</li>
 *     <li>entityId: entityId</li>
 *     <li>createTime: createTime</li>
 *     <li>context: context</li>
 *     <li>params: params</li>
 * </ul>
 */
public class LocalEvent {

    private Byte syncFlag;
    private String eventName;
    private String entityType;
    private Long entityId;
    private Long createTime;
    private LocalEventContext context;
    private Map<String, Object> params;

}
