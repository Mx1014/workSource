// @formatter:off
package com.everhomes.rest.aclink;
import com.everhomes.util.StringHelper;

import java.io.File;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>name:程序名称</li>
 * <li>downloadUrl:固件程序url</li>
 * <li>type:固件程序类型 0：蓝牙 1：wifi</li>
 * <li>size: 程序大小</li>
 * <li>status: 状态 0：失效 1：有效</li>
 * </ul>
 */
public class uploadFirmwarePackageCommand {
    public Long id;
    public String name;
    public String downloadUrl;
    public Byte type;
    public Integer size;
//    public Timestamp createTime;
    public Byte status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

//    public Timestamp getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Timestamp createTime) {
//        this.createTime = createTime;
//    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
