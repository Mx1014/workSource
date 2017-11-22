package com.everhomes.rest.point;

/**
 * <ul>
 *     <li>moduleId: moduleId</li>
 *     <li>operateType: operateType {@link com.everhomes.rest.point.PointOperateType}</li>
 *     <li>status: status {@link com.everhomes.rest.point.PointCommonStatus}</li>
 *     <li>pageAnchor: pageAnchor</li>
 *     <li>pageSize: pageSize</li>
 * </ul>
 */
public class ListPointRulesCommand {

    private Long moduleId;
    private Byte operateType;
    private Byte status;

    private Long pageAnchor;
    private Integer pageSize;
}
