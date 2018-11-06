package com.everhomes.buttscript.engine;

import com.everhomes.bus.LocalEvent;
import com.everhomes.buttscript.*;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.gogs.GogsRepo;
import com.everhomes.gogs.GogsService;
import com.everhomes.rest.buttscript.SyncCode;
import com.everhomes.rest.user.UserDTO;
import com.everhomes.scriptengine.ScriptEngineService;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedTransferQueue;

@Service
public  class ButtScriptSchedulerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptSchedulerMain.class);


    @Autowired
    private ButtScriptConfigProvider buttScriptConfigProvider ;


    @Autowired
    private ButtInfoTypeEventMappingProvider buttInfoTypeEventMappingProvider ;

    @Autowired
    private ButtScriptPublishInfoProvider buttScriptPublishInfoProvider ;

    @Autowired
    private GogsService gogsService ;

    @Autowired
    private ScriptEngineService scriptEngineService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 该返回JSON形式的字符串:
     * 1.对于异步脚本,目前正常执行的返回值为null;
     * 2.对于同步脚本,返回执行结果的JSON形式的字符串;
     * 3.ButtScriptSchedulerResultLog 该类用于日志,它用于记录一个事件所对应的所有脚本的
     * 基本情况(及部分脚本执行结果:同步脚本及不执行脚本的结果,对于异步的脚本不能马上预知结果,会有单体类进去跟踪打印 )
     * 4.EventResultLog 单体类,用于跟踪各脚本的执行情况.
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
            resultLog.setErrorMsg("can not found any mapping to this event");
            LOGGER.info(StringHelper.toJsonString(resultLog));
            return null;
        }
        //返回结果已按异步优先在前排序
        for(ButtInfoTypeEventMapping bte : buttList){
            ButtScriptPublishInfo publishInfo = buttScriptPublishInfoProvider.getButtScriptPublishInfo(namespaceId,bte.getInfoType());
            if(publishInfo == null){//当没有发布的脚本时
                addResultLog(resultLog,bte,null,null);
                continue;
            }
            //目前设置方案是一个事件中只允许执行一个同步脚本,
            // 该脚本会在处理完所有异步脚本后执行,遇到同步脚本,执行一次就退出了
            if(SyncCode.FALSE.getCode().equals(bte.getSyncFlag())){
               //执行同步脚本并得到返回值
                Object obj = null ;
               try{
                   //执行同步脚本
                    obj =   doButtScriptTransfer(bte,publishInfo.getCommitVersion(),localEvent);
                   addResultLog(resultLog,bte,publishInfo.getCommitVersion(),StringHelper.toJsonString(obj));
               }catch(Exception e){
                   addResultLog(resultLog,bte,publishInfo.getCommitVersion(),StringHelper.toJsonString(e));
                   LOGGER.info(StringHelper.toJsonString(resultLog));
                   throw e ;
               }
                LOGGER.info(StringHelper.toJsonString(resultLog));
                return StringHelper.toJsonString(obj) ;
            }
            //对于异步脚本(一般来说1表示异步,但此处认为非0的都是异步的,异步优先嘛,也容忍那些忘了设置的)
            doButtScriptAsync(bte,publishInfo.getCommitVersion(),localEvent);
            addResultLog(resultLog,bte,publishInfo.getCommitVersion(),null);
        }
        LOGGER.info(StringHelper.toJsonString(resultLog));
        return null;

    }

    /**
     * 添加脚本执行记录
     * @param resultLog
     * @param mapping
     */
    private void addResultLog(ButtScriptSchedulerResultLog resultLog, ButtInfoTypeEventMapping mapping,String commitVersion,String result){
        if(resultLog == null || mapping == null){
            return ;
        }
        EventResultLog log = new EventResultLog();
        log.setSyncFlag(mapping.getSyncFlag());
        log.setInfoType(mapping.getInfoType());
        log.setResult(result);
        if(StringUtils.isBlank(commitVersion)){
            log.setResult("there is not any publish script .");
        }else{
            log.setCommitVersion(commitVersion);
        }

    }


    /**
     * 执行异步脚本
     * @param mapping
     * @param lastCommit
     * @return
     */
    private void doButtScriptAsync(ButtInfoTypeEventMapping mapping ,String lastCommit ,LocalEvent event){
        String path = this.getPath(mapping.getNamespaceId());
        GogsRepo anyRepo = getRepo(mapping);
        Map<String, Object> param = new HashMap<>(3);
        param.put("scriptPath", path);// 脚本路径
        param.put("lastCommit", lastCommit);// 版本ID
        param.put("gogsRepo", anyRepo);
        ButtScriptParameter buttScriptParameter = new ButtScriptParameter();
        buttScriptParameter.setEvent(event);
        buttScriptParameter.setOperator(getOperator(event.getContext().getNamespaceId(),event.getContext().getUid()));
        scriptEngineService.push(new ButtScriptAsyncEngine(param,buttScriptParameter));
    }

    /**
     * 执行同步脚本
     * @param mapping
     * @param lastCommit
     * @param event
     * @return
     */
    private Object doButtScriptTransfer(ButtInfoTypeEventMapping mapping ,String lastCommit ,LocalEvent event){
        String path = this.getPath(mapping.getNamespaceId());
        GogsRepo anyRepo = getRepo(mapping);
        Map<String, Object> param = new HashMap<>(3);
        param.put("scriptPath", path);// 脚本路径
        param.put("lastCommit", lastCommit);// 版本ID
        param.put("gogsRepo", anyRepo);
        LinkedTransferQueue<Object> transferQueue = new LinkedTransferQueue<>();
        ButtScriptParameter buttScriptParameter = new ButtScriptParameter();
        buttScriptParameter.setEvent(event);
        buttScriptParameter.setOperator(getOperator(event.getContext().getNamespaceId(),event.getContext().getUid()));
        scriptEngineService.push(new ButtScriptTransferEngine(transferQueue, param,buttScriptParameter));
        // 这里就获取到了 processInternal 的返回结果 result
        Object result = scriptEngineService.poll(transferQueue);
        return result ;
    }

    /**
     * 获取仓库信息
     * @param mapping
     * @return
     */
    private GogsRepo getRepo(ButtInfoTypeEventMapping mapping ){
        //先从配置表获取相关配置信息
        ButtScriptConfig cof = buttScriptConfigProvider.findButtScriptConfig(mapping.getNamespaceId() ,mapping.getInfoType());
        if(cof == null){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not found and buttScriptConfig info ");
        }
        //组装入参
        String path = this.getPath(mapping.getNamespaceId());
        GogsRepo repo = gogsService.getAnyRepo( ButtScriptConstants.GOS_NAMESPACEID,  cof.getModuleType(),  cof.getModuleId(),  cof.getOwnerType(),  cof.getOwnerId());
        return  repo;
    }


    /**
     * 组装path
     * @param namespaceId
     * @return
     */
    private String getPath(Integer namespaceId){
        return  ButtScriptConstants.PRE_PATH+ namespaceId;
    }

    /**
     * 获取当前环境用户的信息
     * @return
     */
    private UserDTO getOperator(Integer namespaceId ,Long operatorUid){

        LOGGER.info("there is get namespaceId:{} ,operatorUid:{}",namespaceId ,operatorUid);
        User user = userProvider.findUserById(operatorUid);

        if(user != null){
            UserDTO dto = ConvertHelper.convert(user, UserDTO.class);
            UserIdentifier identifier = userProvider.findUserIdentifiersOfUser(operatorUid,namespaceId);
            if(identifier != null){
                dto.setIdentifierToken(identifier.getIdentifierToken());
                dto.setIdentifierType(identifier.getIdentifierType());
                dto.setBirthday(user.getBirthday()==null?"":StringHelper.toJsonString(user.getBirthday()) );
                dto.setName(user.getNickName());
                dto.setAvatarUrl(user.getAvatar());
            }
            return dto ;
        }
        LOGGER.info("can not found any user by  namespaceId:{} ,operatorUid:{}",namespaceId ,operatorUid);
        return null ;
    }
}
