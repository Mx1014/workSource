package com.everhomes.buttscript;

import com.everhomes.configurations.ConfigurationsAdminProviderImpl;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.gogs.GogsRepo;
import com.everhomes.gogs.GogsService;
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
import java.util.List;

@Service
public class ButtScriptServiceImpl implements ButtScriptService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptServiceImpl.class);

    @Autowired
    private ButtScriptPublishInfoProvider buttScriptPublishInfoProvider ;

    @Autowired
    private ButtScriptConfigProvider buttScriptConfigProvider ;

    @Autowired
    private GogsService gogsService ;

    /**
     * 用于定义每个仓库中的文件路径前缀 ,文件路径定义为:repo_999957(repo_+域空间ID)
     * 每一种infotype 拥有一个仓库,靠文件路径来区分不同的脚本,每个域空间都有自己的文件路径
     * 也就是说eh_butt_script_config 这张表中的数据,只要info_type字段是相同的话,module_id/module_type/owner_id/owner_type也是相同的
     */
    private static final String PRE_PATH = "repo_";

    /**
     * 在此功能中创建仓库时所用入参namespaceID皆为0,也就是不按域空间来区分库,
     * 而是按eh_butt_script_config 中的info_type来区分,避免创建多个库.因为有可能
     * 多个域空间都有同一个info_type.为方便管理,把它们放到同一个库中.不同的域空间的
     * 脚本在该空间下靠path区分.
     */
    private static final Integer GOS_NAMESPACEID = 0;

    /**
     * 按域空间查询脚本信息接口
     * @param cmd
     * GogsRepo repo, String path, String lastCommit
     * lastCommit 为空表示获取最新版本
     * @return
     */
    @Override
    public ScriptDTO getScriptByNamespace(GetScriptCommand cmd) {
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        //先从配置表获取相关配置信息
        ButtScriptConfig cof = buttScriptConfigProvider.findButtScriptConfig(namespaceId ,cmd.getInfoType());
        if(cof == null){
            if(namespaceId == null){
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "can not found and buttScriptConfig info .namespaceId:{};infoType:{} .",namespaceId,cmd.getInfoType());
            }
        }
        //组装入参
        String lastCommit = cmd.getCommitVersion();
        String path = this.getPath(namespaceId);
        GogsRepo repo = gogsService.getAnyRepo( GOS_NAMESPACEID,  cof.getModuleType(),  cof.getModuleId(),  cof.getOwnerType(),  cof.getOwnerId());
        //获取仓库中的文件
        byte[] file = gogsService.getFile(repo, path, lastCommit);
        //组装返回值
        String script = new String(file, Charset.forName("UTF-8"));
        ScriptDTO dto = new ScriptDTO();
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
        //先检查有没有建仓库,没有得先建仓库

        //保存脚本到仓库中,返回版本号
        //如果选择了发布,在发布信息表创建或更新一条数据

        return null;
    }

    /**
     * 3)通过域空间查询所有版本脚本信息接口
     * @param cmd
     * @return
     */
    @Override
    public ScriptVersionInfoResponse findScriptVersionInfoByNamespaceId(FindScriptVersionInfoCommand cmd) {
        return null;
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
        List<ButtScriptConfig> list =  buttScriptConfigProvider.findButtScriptConfigByNamespaceId(namespaceId);
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
        return  PRE_PATH+ namespaceId;
    }
}
