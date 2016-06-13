package com.everhomes.rest.point;

import com.everhomes.rest.point.PointType;
import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>id:流水号</li>
 *<li>ownerUid:用户id</li>
 *<li>scoreType:积分类型 {@link PointType}</li>
 *<li>score:分数</li>
 *<li>operatorUid:操作人，一般默认本人，可能涉及到管理员加分</li>
 *<li>operateTime:操作时间</li>
 *<li>createTime:创建时间</li>
 *</ul>
 */
public class UserScoreDTO {
    private java.lang.Long id;
    private java.lang.Long ownerUid;
    private java.lang.String scoreType;
    private java.lang.Integer score;
    private java.lang.Long operatorUid;
    private java.sql.Timestamp operateTime;
    private java.sql.Timestamp createTime;

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(java.lang.Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public java.lang.String getScoreType() {
        return scoreType;
    }

    public void setScoreType(java.lang.String scoreType) {
        this.scoreType = scoreType;
    }

    public java.lang.Integer getScore() {
        return score;
    }

    public void setScore(java.lang.Integer score) {
        this.score = score;
    }

    public java.lang.Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(java.lang.Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public java.sql.Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(java.sql.Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public java.sql.Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.sql.Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
