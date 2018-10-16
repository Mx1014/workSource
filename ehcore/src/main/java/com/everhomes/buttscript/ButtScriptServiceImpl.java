package com.everhomes.buttscript;

import com.everhomes.configurations.ConfigurationsAdminProviderImpl;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.gogs.*;
import com.everhomes.rest.buttscript.*;
import com.everhomes.user.UserContext;
import com.everhomes.util.DateUtils;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ButtScriptServiceImpl implements ButtScriptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptServiceImpl.class);

    @Autowired
    private ButtScriptPublishInfoProvider buttScriptPublishInfoProvider ;

    @Autowired
    private ButtScriptConfigProvider buttScriptConfigProvider ;

    @Autowired
    private ButtScriptLastCommitProvider buttScriptLastCommitProvider ;

    @Autowired
    private GogsService gogsService ;



    /**
     * 按域空间查询脚本信息接口
     * @param cmd
     * GogsRepo repo, String path, String lastCommit
     * lastCommit 为空表示获取最新版本
     * @return
     */
    @Override
    public ScriptDTO getScriptByNamespace(GetScriptCommand cmd) {
        ScriptDTO dto = new ScriptDTO();
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //先从配置表获取相关配置信息
        ButtScriptConfig cof = buttScriptConfigProvider.findButtScriptConfig(namespaceId ,cmd.getInfoType());
        if(cof == null){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "can not found and buttScriptConfig info .namespaceId:{};infoType:{} .",namespaceId,cmd.getInfoType());
        }
        //组装入参
        String lastCommit = cmd.getCommitVersion();
        String path = this.getPath(namespaceId);
        GogsRepo repo = gogsService.getAnyRepo( ButtScriptConstants.GOS_NAMESPACEID,  cof.getModuleType(),  cof.getModuleId(),  cof.getOwnerType(),  cof.getOwnerId());
        if(repo == null){
            return dto ;
        }
        //获取仓库中的文件
        byte[] file = gogsService.getFile(repo, path, lastCommit);
        //组装返回值
        String script = new String(file, Charset.forName("UTF-8"));
        dto.setCommitVersion(lastCommit);
        dto.setInfoType(cmd.getInfoType());
        dto.setNamespaceId(namespaceId);
        dto.setScript(script);
        return dto;
    }

    /**
     * 2)保存脚本信息接口(含保存并发布)
     * @param cmd
     * @return
     */
    @Override
    public ScriptVersionInfoDTO saveScript(SaveScriptCommand cmd) {
        //1)先检查有没有建仓库,没有得先建仓库
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //先从配置表获取相关配置信息
        ButtScriptConfig cof = buttScriptConfigProvider.findButtScriptConfig(namespaceId ,cmd.getInfoType());
        if(cof == null){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "can not found and buttScriptConfig info .namespaceId:{};infoType:{} .",namespaceId,cmd.getInfoType());
        }
        GogsRepo repo = gogsService.getAnyRepo( ButtScriptConstants.GOS_NAMESPACEID,  cof.getModuleType(),  cof.getModuleId(),  cof.getOwnerType(),  cof.getOwnerId());
        if(repo == null){//建仓库
            repo = new GogsRepo();
            repo.setName(cof.getInfoType());
            repo.setNamespaceId(ButtScriptConstants.GOS_NAMESPACEID);
            repo.setModuleType(cof.getModuleType());
            repo.setModuleId(cof.getModuleId());
            repo.setOwnerType(cof.getOwnerType());
            repo.setOwnerId( cof.getOwnerId());
            repo.setRepoType(GogsRepoType.NORMAL.name());
            repo = gogsService.createRepo(repo);
        }
        //保存脚本到仓库中,返回版本号
        GogsRawFileParam param = new GogsRawFileParam();
        String path = this.getPath(namespaceId);
        //尝试获取仓库中的最后一次提交信息,若能获取到,说明
        ButtScriptLastCommit lastCommitInfo = buttScriptLastCommitProvider.getButtScriptLastCommit(namespaceId ,cmd.getInfoType());
        param.setCommitMessage("commit "+path);
        param.setContent(cmd.getScript());
        param.setNewFile(true);
        if(lastCommitInfo !=null){
            param.setLastCommit(lastCommitInfo.getLastCommit());
            param.setNewFile(false);
        }

        GogsCommit gogsCommit = gogsService.commitFile(repo, path, param);
        //更新版本号表的信息
        if(lastCommitInfo !=null){
            lastCommitInfo.setCommitTime(DateUtils.currentTimestamp());
            lastCommitInfo.setLastCommit(gogsCommit.getId());
            LOGGER.info("update ButtScriptLastCommit info .lastCommit:{}",gogsCommit.getId());
            buttScriptLastCommitProvider.updateButtScriptLastCommit(lastCommitInfo);
        }else{
            lastCommitInfo = new ButtScriptLastCommit();
            lastCommitInfo.setLastCommit(gogsCommit.getId());
            lastCommitInfo.setCommitTime(DateUtils.currentTimestamp());
            lastCommitInfo.setInfoType(cmd.getInfoType());
            lastCommitInfo.setNamespaceId(namespaceId);
            LOGGER.info("add ButtScriptLastCommit info .lastCommit:{}",gogsCommit.getId());
            buttScriptLastCommitProvider.crteateButtScriptLastCommit(lastCommitInfo);

        }
        ScriptVersionInfoDTO dto = new ScriptVersionInfoDTO();
        dto.setPublishCode(PublishCode.FALSE.getCode());
        //如果选择了发布,在发布信息表创建或更新一条数据
        if(PublishCode.TRUE.getCode().equals(cmd.getPublishCode())){
            ButtScriptPublishInfo publishInfo = buttScriptPublishInfoProvider.getButtScriptPublishInfo(namespaceId ,cmd.getInfoType());
            if(publishInfo == null){
                publishInfo = new ButtScriptPublishInfo();
                publishInfo.setNamespaceId(namespaceId);
                publishInfo.setInfoType(cmd.getInfoType());
                publishInfo.setPublishTime(DateUtils.currentTimestamp());
                publishInfo.setCommitVersion(gogsCommit.getId());
                LOGGER.info("add ButtScriptPublishInfo  .commitVersion:{}",gogsCommit.getId());
                buttScriptPublishInfoProvider.crteateButtScriptPublishInfo(publishInfo);
            }else{
                publishInfo.setCommitVersion(gogsCommit.getId());
                publishInfo.setPublishTime(DateUtils.currentTimestamp());
                LOGGER.info("update ButtScriptPublishInfo  .commitVersion:{}",gogsCommit.getId());
                buttScriptPublishInfoProvider.updateButtScriptPublishInfo(publishInfo);
            }

            dto.setPublishCode(PublishCode.TRUE.getCode());
            dto.setPublishTime(DateUtils.currentTimestamp());
        }

        dto.setCommitVersion(gogsCommit.getId());
        dto.setCreateTime(gogsCommit.getCommitTime()==null?null:new Timestamp(gogsCommit.getCommitTime().getTime()));
        dto.setId(gogsCommit.getId());
        dto.setInfoType(cmd.getInfoType());
        dto.setNamespaceId(namespaceId);

        return dto;
    }

    /**
     * 3)通过域空间查询所有版本脚本信息接口
     * @param cmd
     * @return
     */
    @Override
    public ScriptVersionInfoResponse findScriptVersionInfoByNamespaceId(FindScriptVersionInfoCommand cmd) {
        ScriptVersionInfoResponse res = new ScriptVersionInfoResponse();
        //获取配置信息
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //先从配置表获取相关配置信息
        ButtScriptConfig cof = buttScriptConfigProvider.findButtScriptConfig(namespaceId ,cmd.getInfoType());
        if(cof == null){
            LOGGER.info("can not found  buttScriptConfig info .");
            return res ;
            /*throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not found  buttScriptConfig info .namespaceId:{};infoType:{} .",namespaceId,cmd.getInfoType());*/

        }
        //获取仓库
        GogsRepo repo = gogsService.getAnyRepo( ButtScriptConstants.GOS_NAMESPACEID,  cof.getModuleType(),  cof.getModuleId(),  cof.getOwnerType(),  cof.getOwnerId());
        if(repo == null){
            return res ;
        }
        //查询所有版本脚本
        List<GogsCommit> gogsList = gogsService.listCommits(repo,this.getPath(namespaceId),cmd.getPageOffset()== null ? 1 : cmd.getPageOffset(),cmd.getPageSize());
        ButtScriptPublishInfo publishInfo = buttScriptPublishInfoProvider.getButtScriptPublishInfo(namespaceId ,cmd.getInfoType());
        if(gogsList != null && gogsList.size()>0){
            //组装返回值
            List<ScriptVersionInfoDTO> dtos = new ArrayList<ScriptVersionInfoDTO>();
             String publishVersion="";
             Timestamp publishTime = null;
             if(publishInfo != null){
                 publishVersion = publishInfo.getCommitVersion();
                 publishTime = publishInfo.getPublishTime();
             }
             for(GogsCommit gogs : gogsList){
                 ScriptVersionInfoDTO dto = new ScriptVersionInfoDTO();
                 dto.setPublishCode(PublishCode.FALSE.getCode());
                 if(gogs.getId().equals(publishVersion)){
                     dto.setPublishCode(PublishCode.TRUE.getCode());
                     dto.setPublishTime(publishTime);
                 }
                 dto.setNamespaceId(namespaceId);
                 dto.setInfoType(cmd.getInfoType());
                 dto.setCommitVersion(gogs.getId());
                 dto.setCreateTime(gogs.getCommitTime()==null?null:new Timestamp(gogs.getCommitTime().getTime()));
                 dto.setId(gogs.getId());

                 dtos.add(dto);
             }
            res.setDtos(dtos);
        }
        res.setNextPageOffset(cmd.getPageOffset()==null?1:cmd.getPageOffset() + 1);
        return res;
    }

    /**
     * 4)发布版本接口
     * @param cmd
     */
    @Override
    public void publishScriptVersion(PublishScriptVersionCommand cmd) {

        if(StringUtils.isBlank(cmd.getCommitVersion())){
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "commitVersion is null  .");
        }
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //1.先检查该域空间下该分类是否已有数据,有说明存在已发布的版本
        ButtScriptPublishInfo info = buttScriptPublishInfoProvider.getButtScriptPublishInfo(namespaceId , cmd.getInfoType());
        //2.存在发布版本时,直接更新
        if(info != null){
             info.setCommitVersion(cmd.getCommitVersion());
             info.setPublishTime(DateUtils.currentTimestamp());
             LOGGER.debug("update publish  version {}",cmd.getCommitVersion());
             buttScriptPublishInfoProvider.updateButtScriptPublishInfo(info);

        }else{//3.无已发布版本时,直接新建
            info = new ButtScriptPublishInfo();
            info.setCommitVersion(cmd.getCommitVersion());
            info.setPublishTime(DateUtils.currentTimestamp());
            info.setInfoType(cmd.getInfoType());
            info.setNamespaceId(cmd.getNamespaceId());
            LOGGER.debug("insert publish  version {}",cmd.getCommitVersion());
            buttScriptPublishInfoProvider.crteateButtScriptPublishInfo(info);
        }

    }

    /**
     *5)查询域空间下脚本分类接口
     * @param cmd
     * @return
     */
    @Override
    public ScriptnInfoTypeResponse findScriptInfoType(FindScriptInfoTypeCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        List<ButtScriptConfig> list =  buttScriptConfigProvider.findButtScriptConfigByNamespaceId(namespaceId , (byte) 1);
        ScriptnInfoTypeResponse res = new ScriptnInfoTypeResponse();
        if(list != null && list.size()>0){
            for(ButtScriptConfig conf :list){
                ScriptInfoTypeDTO dto = new ScriptInfoTypeDTO();
                dto.setId(conf.getId());
                dto.setInfoDescribe(conf.getInfoDescribe());
                dto.setInfoType(conf.getInfoType());
                dto.setNamespaceId(conf.getNamespaceId());
                res.getDtos().add(dto);
            }
        }
        return res;
    }

    /**
     * 6)取消版本发布接口
     * @param cmd
     */
    @Override
    public void publishScriptVersionCancel(PublishScriptVersionCancelCommand cmd) {

        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

        //取消版本发布的前提是已发布了版本
        //1.先检查该域空间下该分类是否已有数据,有说明存在已发布的版本
        ButtScriptPublishInfo info = buttScriptPublishInfoProvider.getButtScriptPublishInfo(namespaceId , cmd.getInfoType());
        if(info == null){//信息有误,该域空间该类型并无发布信息
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "can not found any publish info with params namespaceId:{} ; infoType:{}  .",namespaceId ,cmd.getInfoType());
        }

        //取消即删除记录信息
        buttScriptPublishInfoProvider.deleteButtScriptPublishInfo(info);
    }

    private String getPath(Integer namespaceId){
        return  ButtScriptConstants.PRE_PATH+ namespaceId;
    }
}
