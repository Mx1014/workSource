package com.everhomes.rest.point;

import com.everhomes.rest.point.PointType;
import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *         <ul>
 *         <li>operatorUid:操作者ID</li>
 *         <li>pointType:积分类型{@link PointType}</li>
 *         <li>point:分数</li>
 *         <li>uid:用户ID</li>
 *         </ul>
 */
public class AddUserPointCommand {
    private Long operatorUid;
    private String pointType;
    private Integer point;
    private Long uid;

    public AddUserPointCommand() {
    }

    public AddUserPointCommand(Long operatorUid, String pointType, Integer point, Long uid) {
        super();
        this.operatorUid = operatorUid;
        this.pointType = pointType;
        this.point = point;
        this.uid = uid;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
