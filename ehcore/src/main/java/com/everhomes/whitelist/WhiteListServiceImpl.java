// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.whitelist.*;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WhiteListServiceImpl implements WhiteListSerivce{

    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteListServiceImpl.class);

    @Autowired
    private WhiteListProvider whiteListProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public void createWhiteList(CreateWhiteListCommand cmd) {
        if (null == cmd.getPhoneNumber() || "".equals(cmd.getPhoneNumber())) {
            LOGGER.error("PhoneNumber cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "PhoneNumber cannot be null.");
        }
        if (null == cmd.getNamespaceId()) {
            LOGGER.error("namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId cannot be null.");
        }

        User user = UserContext.current().getUser();
        PhoneWhiteList phoneWhiteList = new PhoneWhiteList();
        phoneWhiteList.setPhoneNumber(cmd.getPhoneNumber());
        phoneWhiteList.setNamespaceId(cmd.getNamespaceId());
        phoneWhiteList.setCreatorUid(user.getId());
        phoneWhiteList.setCreatorTime(new Timestamp(System.currentTimeMillis()));
        whiteListProvider.createWhiteList(phoneWhiteList);
    }

    @Override
    public void batchCreateWhiteList(BatchCreateWhiteListCommand cmd) {
        List<PhoneWhiteList> list = new ArrayList<>();
        for (CreateWhiteListCommand createWhiteListCommand :cmd.getCreateWhiteListCommandList()) {
            if (createWhiteListCommand != null) {
                if (null == createWhiteListCommand.getPhoneNumber() || "".equals(createWhiteListCommand.getPhoneNumber())) {
                    LOGGER.error("PhoneNumber cannot be null.");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "PhoneNumber cannot be null.");
                }
                if (null == createWhiteListCommand.getNamespaceId()) {
                    LOGGER.error("namespaceId cannot be null.");
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "namespaceId cannot be null.");
                }

                User user = UserContext.current().getUser();
                PhoneWhiteList phoneWhiteList = new PhoneWhiteList();
                phoneWhiteList.setPhoneNumber(createWhiteListCommand.getPhoneNumber());
                phoneWhiteList.setNamespaceId(createWhiteListCommand.getNamespaceId());
                phoneWhiteList.setCreatorUid(user.getId());
                phoneWhiteList.setCreatorTime(new Timestamp(System.currentTimeMillis()));
                list.add(phoneWhiteList);
            }
        }
        whiteListProvider.batchCreateWhiteList(list);
    }

    @Override
    public void deleteWhiteList(DeleteWhiteListCommand cmd) {
        PhoneWhiteList phoneWhiteList = whiteListProvider.getWhiteList(cmd.getId());
        if (null == phoneWhiteList) {
            LOGGER.error("PhoneWhiteList not found, cmd={}",cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "PhoneWhiteList not found.");
        }

        whiteListProvider.deleteWhiteList(phoneWhiteList);
    }

    @Override
    public void batchDeleteWhiteList(BatchDeleteWhiteListCommand cmd) {
        for (Long id : cmd.getIds()) {
            if (null != id) {
                PhoneWhiteList phoneWhiteList = whiteListProvider.getWhiteList(id);
                if (null == phoneWhiteList) {
                    LOGGER.error("PhoneWhiteList not found, id={}",id);
                    throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                            "PhoneWhiteList not found.");
                }
                whiteListProvider.deleteWhiteList(phoneWhiteList);
            }
        }
    }

    @Override
    public void updateWhiteList(UpdateWhiteListCommand cmd) {
        PhoneWhiteList phoneWhiteList = whiteListProvider.getWhiteList(cmd.getId());
        if (null == phoneWhiteList) {
            LOGGER.error("PhoneWhiteList not found, cmd={}",cmd);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "PhoneWhiteList not found.");
        }

        if (null != cmd.getPhoneNumber()) {
           phoneWhiteList.setPhoneNumber(cmd.getPhoneNumber());
        }

        whiteListProvider.updateWhiteList(phoneWhiteList);
    }

    @Override
    public WhiteListDTO getWhiteList(GetWhiteListCommand cmd) {
        if(null == cmd.getId()) {
            LOGGER.error("ID cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "ID cannot be null.");
        }
        return ConvertHelper.convert(whiteListProvider.getWhiteList(cmd.getId()), WhiteListDTO.class);
    }

    @Override
    public ListWhiteListResponse listWhiteList(ListWhiteListCommand cmd) {
        ListWhiteListResponse response = new ListWhiteListResponse();
        cmd.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));
        List<PhoneWhiteList> list = whiteListProvider.listWhiteList(cmd.getPhoneNumber(), cmd.getPageAnchor(), cmd.getPageSize());
        int size = list.size();
        if (size >0) {
            response.setWhiteListDTOList(list.stream().map(r -> {
                WhiteListDTO whiteListDTO = ConvertHelper.convert(r, WhiteListDTO.class);
                return  whiteListDTO;
            }).collect(Collectors.toList()));
            if (size != cmd.getPageSize()) {
                response.setNextAnchor(null);
            }else {
                response.setNextAnchor(list.get(size - 1).getId());
            }
        }
        return response;
    }
}
