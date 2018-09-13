package com.everhomes.buttscript.engine;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件日志记录器
 * <ul>
 * <li>infoType: 脚本分类</li>
 * <li>commitVersion: 脚本版本号</li>
 * <li>syncFlag: 同步还是异步</li>
 * <li>result: 执行结果</li>
 * </ul>
 */
public class ButtScriptSchedulerResultLog {

    private String eventName ;
    private Integer namespaceId ;
    private List<EventResultLog> logs ;
    private String errorMsg ;

    public ButtScriptSchedulerResultLog(){
        if(logs == null){
            logs = new ArrayList<EventResultLog>();
        }
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<EventResultLog> getLogs() {
        return logs;
    }

    public void setLogs(List<EventResultLog> logs) {
        this.logs = logs;
    }
}
