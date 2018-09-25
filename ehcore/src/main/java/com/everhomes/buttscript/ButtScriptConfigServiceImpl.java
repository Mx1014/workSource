package com.everhomes.buttscript;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.rest.buttscript.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ButtScriptConfigServiceImpl implements  ButtScriptConfigService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtScriptConfigServiceImpl.class);

    @Autowired
    private  ButtScriptConfigProvider buttScriptConfigProvider ;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public ButtScriptConfingResponse findButtScriptConfigByNamespaceId(FindButtScriptConfingCommand cmd) {
        //若前台无每页数量传来则取默认配置的
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        //创建锚点分页所需的对象
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        //主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        //Provider 层传进行查询并返回对象
        List<ButtScriptConfig> boList = buttScriptConfigProvider.findButtScriptConfigByNamespaceId(cmd.getNamespaceId(),cmd.getStatus());

        //对象转换
        ButtScriptConfingResponse returnDto = new ButtScriptConfingResponse ();

        if(boList == null){
            return returnDto;
        }

        returnDto.setDtos(boList.stream().map(r->{
            //copy 相同属性下的值
            ButtScriptConfigDTO dto = ConvertHelper.convert(r, ButtScriptConfigDTO.class);
            return dto;
        }).collect(Collectors.toList()));

        //设置下一页开始锚点
        returnDto.setNextPageAnchor(locator.getAnchor());
        return returnDto;
    }

    @Override
    public void updateButtScriptConfing(UpdateButtScriptConfingCommand cmd) {
        ButtScriptConfig bo = buttScriptConfigProvider.getButtScriptConfigById(cmd.getId());
        bo.setInfoDescribe(cmd.getInfoDescribe());
        bo.setRemark(cmd.getRemark());
        buttScriptConfigProvider.updateButtScriptConfig(bo);
    }

    @Override
    public ButtScriptConfigDTO crteateButtScriptConfing(AddButtScriptConfingCommand cmd) {
        //如果传参对象为空或infoType 为空或空字符串 或 eventName 为空 ，抛出异常
        if(cmd == null
                || StringUtils.isBlank(cmd.getInfoType())
                ||StringUtils.isBlank(cmd.getInfoType())
                ||StringUtils.isBlank(cmd.getModuleType())
                ||null == cmd.getModuleId()
                ) {
            String msg = "some parameters must not  be null.";
            throwSelfDefNullException(msg);
        }
        //主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        ButtScriptConfig bo = ConvertHelper.convert(cmd, ButtScriptConfig.class);
        bo.setOwnerId(cmd.getModuleId());
        bo.setOwnerType(cmd.getModuleType());
        if(bo.getStatus()==null){
            bo.setStatus(TrueOrFalseCode.TRUE.getCode());
        }
        bo = buttScriptConfigProvider.crteateButtScriptConfig(bo);
        return ConvertHelper.convert(bo, ButtScriptConfigDTO.class);
    }

    @Override
    public void updateButtScriptConfingStatus(UpdateButtScriptConfingStatusCommand cmd) {

        ButtScriptConfig bo = buttScriptConfigProvider.getButtScriptConfigById(cmd.getId());
        bo.setStatus(cmd.getStatus());
        buttScriptConfigProvider.updateButtScriptConfig(bo);
    }

    /**
     * 抛出对象或其ID属性为空的异常
     * @param msg 报错信息
     */
    private void throwSelfDefNullException(String msg ){
        LOGGER.error(msg);
        throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,msg);
    }
}
