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
public class ButtInfoTypeEventMappingServiceImpl  implements ButtInfoTypeEventMappingService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ButtInfoTypeEventMappingServiceImpl.class);

    @Autowired
    private  ButtInfoTypeEventMappingProvider buttInfoTypeEventMappingProvider ;

    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    public ButtInfoTypeEventMappingResponse findButtEventMapping(FindButtEventMappingCommand cmd) {
        //若前台无每页数量传来则取默认配置的
        int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());
        //创建锚点分页所需的对象
        CrossShardListingLocator locator = new CrossShardListingLocator();
        locator.setAnchor(cmd.getPageAnchor());
        //主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        //Provider 层传进行查询并返回对象
        List<ButtInfoTypeEventMapping> boList = buttInfoTypeEventMappingProvider.findByInfoType(cmd.getInfoType(),cmd.getNamespaceId());

        //对象转换
        ButtInfoTypeEventMappingResponse returnDto = new ButtInfoTypeEventMappingResponse ();
        if(boList == null){
                return returnDto ;
        }
        returnDto.setDtos(boList.stream().map(r->{
            //copy 相同属性下的值
            ButtInfoTypeEventMappingDTO dto = ConvertHelper.convert(r, ButtInfoTypeEventMappingDTO.class);
            return dto;
        }).collect(Collectors.toList()));

        //设置下一页开始锚点
        returnDto.setNextPageAnchor(locator.getAnchor());
        return returnDto;
    }

    @Override
    public void updateButtEventMapping(UpdateButtEventMappingCommand cmd) {
        //如果传参对象为空或infoType 为空或空字符串 或 eventName 为空 ，抛出异常
        if(cmd == null || StringUtils.isBlank(cmd.getInfoType()) ||
                StringUtils.isBlank(cmd.getEventName())) {
            String msg = "cmd or cmd.infoType or cmd.eventName cannot be null.";
            throwSelfDefNullException(msg);
        }
        //主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        ButtInfoTypeEventMapping bo = ConvertHelper.convert(cmd, ButtInfoTypeEventMapping.class);
        buttInfoTypeEventMappingProvider.updateButtInfoTypeEventMapping(bo);
    }

    @Override
    public ButtInfoTypeEventMappingDTO crteateButtEventMapping(AddButtEventMappingCommand cmd) {
        //如果传参对象为空或infoType 为空或空字符串 或 eventName 为空 ，抛出异常
        if(cmd == null || StringUtils.isBlank(cmd.getInfoType()) ||
                StringUtils.isBlank(cmd.getEventName())) {
            String msg = "cmd or cmd.infoType or cmd.eventName cannot be null.";
            throwSelfDefNullException(msg);
        }
        //主要用于对传入的 nameSpaceId 为空时的处理，为空则从当前环境中获取
        Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
        cmd.setNamespaceId(namespaceId);
        ButtInfoTypeEventMapping bo = ConvertHelper.convert(cmd, ButtInfoTypeEventMapping.class);
        bo = buttInfoTypeEventMappingProvider.crteateButtInfoTypeEventMapping(bo);
        return ConvertHelper.convert(bo, ButtInfoTypeEventMappingDTO.class);
    }

    @Override
    public void deleteButtEventMapping(DeleteButtEventMappingCommand cmd) {

        //如果传参对象为空或ID必填项为空，抛出异常
        if(cmd == null || cmd.getId() == null) {
            String msg = "cmd or cmd.id cannot be null.";
            throwSelfDefNullException(msg);
        }
        ButtInfoTypeEventMapping bo = buttInfoTypeEventMappingProvider.getButtInfoTypeEventMappingById(cmd.getId());
        if(bo == null){
            String msg = "can not found anyMapping by id : "+cmd.getId() +".";
            throwSelfDefNullException(msg);
        }
        //调Provider 层进行
        buttInfoTypeEventMappingProvider.deleteButtInfoTypeEventMapping(bo);
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
