package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;
import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

public class DoorCommandDTO {
    private Byte     status;
    private Long     serverKeyVer;
    private Byte     ownerType;
    private String     cmdBody;
    private Long     userId;
    private String     cmdResp;
    private Byte     cmdId;
    private Byte     aclinkKeyVer;
    private Byte     cmdType;
    private Long     doorId;
    private Long     cmdSeq;
    private Long     id;
    private Long     ownerId;

    

    public Byte getStatus() {
        return status;
    }



    public void setStatus(Byte status) {
        this.status = status;
    }



    public Long getServerKeyVer() {
        return serverKeyVer;
    }



    public void setServerKeyVer(Long serverKeyVer) {
        this.serverKeyVer = serverKeyVer;
    }



    public Byte getOwnerType() {
        return ownerType;
    }



    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
    }



    public String getCmdBody() {
        return cmdBody;
    }



    public void setCmdBody(String cmdBody) {
        this.cmdBody = cmdBody;
    }



    public Long getUserId() {
        return userId;
    }



    public void setUserId(Long userId) {
        this.userId = userId;
    }



    public String getCmdResp() {
        return cmdResp;
    }



    public void setCmdResp(String cmdResp) {
        this.cmdResp = cmdResp;
    }



    public Byte getCmdId() {
        return cmdId;
    }



    public void setCmdId(Byte cmdId) {
        this.cmdId = cmdId;
    }



    public Byte getAclinkKeyVer() {
        return aclinkKeyVer;
    }



    public void setAclinkKeyVer(Byte aclinkKeyVer) {
        this.aclinkKeyVer = aclinkKeyVer;
    }



    public Byte getCmdType() {
        return cmdType;
    }



    public void setCmdType(Byte cmdType) {
        this.cmdType = cmdType;
    }



    public Long getDoorId() {
        return doorId;
    }



    public void setDoorId(Long doorId) {
        this.doorId = doorId;
    }



    public Long getCmdSeq() {
        return cmdSeq;
    }



    public void setCmdSeq(Long cmdSeq) {
        this.cmdSeq = cmdSeq;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public Long getOwnerId() {
        return ownerId;
    }



    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}