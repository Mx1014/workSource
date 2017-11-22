package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>startTime: startTime</li>
 *     <li>endTime: endTime</li>
 *     <li>phone: phone</li>
 *     <li>userId: 用户id, 不传就不区分用户</li>
 *     <li>systemId: systemId</li>
 *     <li>operateType: 日志类型 {@link com.everhomes.rest.point.PointOperateType}</li>
 *     <li>pageAnchor: 锚点</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ExportPointLogsCommand {

    private Long startTime;
    private Long endTime;

    private String phone;

    private Long userId;
    private Long systemId;
    private Byte operateType;
    private Long pageAnchor;
    private Integer pageSize;

}
