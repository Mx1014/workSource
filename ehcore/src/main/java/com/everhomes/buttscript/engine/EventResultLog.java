package com.everhomes.buttscript.engine;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>infoType: 脚本分类</li>
 * <li>commitVersion: 脚本版本号</li>
 * <li>syncFlag: 同步还是异步</li>
 * <li>result: 执行结果</li>
 * </ul>
 */
class EventResultLog{
    private String infoType ;
    private String commitVersion ;
    private Byte  syncFlag ;
    private String result ;

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getCommitVersion() {
        return commitVersion;
    }

    public void setCommitVersion(String commitVersion) {
        this.commitVersion = commitVersion;
    }

    public Byte getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Byte syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
