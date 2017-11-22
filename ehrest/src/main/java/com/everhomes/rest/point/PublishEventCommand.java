package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>syncFlag: 同步还是异步</li>
 *     <li>eventJson: 事件json</li>
 * </ul>
 */
public class PublishEventCommand {

    private Byte syncFlag;
    private String eventJson;

}
