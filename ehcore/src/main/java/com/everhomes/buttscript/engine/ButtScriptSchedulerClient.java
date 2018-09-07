package com.everhomes.buttscript.engine;

import com.everhomes.bus.LocalEvent;
import com.everhomes.buttscript.ButtInfoTypeEventMapping;
import com.everhomes.buttscript.ButtInfoTypeEventMappingProvider;
import com.everhomes.buttscript.ButtScriptPublishInfo;
import com.everhomes.buttscript.ButtScriptPublishInfoProvider;
import com.everhomes.util.StringHelper;
import org.jooq.tools.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public  class ButtScriptSchedulerClient {

    @Autowired
    private ButtInfoTypeEventMappingProvider buttInfoTypeEventMappingProvider ;

    @Autowired
    private ButtScriptPublishInfoProvider buttScriptPublishInfoProvider ;

    /**
     * 该返回JSON形式的字符串
     * @param localEvent
     * @return
     */
    public String  doButtScriptMethod(LocalEvent localEvent) {
        //先初始化日志记录器,及设置一些基础信息
        ButtScriptSchedulerResultLog resultLog = new ButtScriptSchedulerResultLog();
        //根据入参(一个从kafka中获取到的事件),从eh_butt_info_type_event_mapping表中
        Integer namespaceId = localEvent.getContext().getNamespaceId();
        String eventName = localEvent.getEventName();
        resultLog.setEventName(eventName);
        resultLog.setNamespaceId(namespaceId);
        List<ButtInfoTypeEventMapping> buttList =  buttInfoTypeEventMappingProvider.findButtInfoTypeEventMapping(eventName ,namespaceId);
        if(buttList == null || buttList.size() <1){
            return StringHelper.toJsonString(resultLog);
        }
        //返回结果已安异步优先在前排序
        for(ButtInfoTypeEventMapping bte : buttList){
            ButtScriptPublishInfo publishInfo = buttScriptPublishInfoProvider.getButtScriptPublishInfo(namespaceId,bte.getInfoType());

        }
        //查询到该事件影响到哪些脚本,然后分别执行这些脚本
        return null;

    }

    /**
     * 添加脚本执行记录
     * @param resultLog
     * @param mapping
     */
    private void addResultLog(ButtScriptSchedulerResultLog resultLog, ButtInfoTypeEventMapping mapping,String commitVersion){
        if(resultLog == null || mapping == null){
            return ;
        }
        EventResultLog log = new EventResultLog();
        log.setSyncFlag(mapping.getSyncFlag());
        log.setInfoType(mapping.getInfoType());
        if(StringUtils.isBlank(commitVersion)){
            log.setResult("there is not any publish script .");
        }else{
            log.setCommitVersion(commitVersion);
        }

    }
}
