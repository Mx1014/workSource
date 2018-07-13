// @formatter:off
package com.everhomes.whitelist;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import org.apache.commons.lang.StringUtils;
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
        if (StringUtils.isBlank(cmd.getPhoneNumber())) {
            LOGGER.error("PhoneNumber cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "PhoneNumber cannot be null.");
        }
        if (null == cmd.getNamespaceId()) {
            LOGGER.error("namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId cannot be null.");
        }

        PhoneWhiteList checkPhoneIsExists = this.whiteListProvider.checkPhoneIsExists(cmd.getNamespaceId(), cmd.getPhoneNumber());

        if (null != checkPhoneIsExists) {
            LOGGER.error("PhoneNumber is exists.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, WhiteListServiceErrorCode.ERROR_WHITELIST_PHONE_IS_EXISTS,
                    "PhoneNumber is exists.");
        }

        User user = UserContext.current().getUser();
        PhoneWhiteList phoneWhiteList = new PhoneWhiteList();
        phoneWhiteList.setPhoneNumber(cmd.getPhoneNumber());
        phoneWhiteList.setNamespaceId(cmd.getNamespaceId());
        phoneWhiteList.setCreatorUid(user.getId());
        phoneWhiteList.setCreateTime(new Timestamp(System.currentTimeMillis()));
        whiteListProvider.createWhiteList(phoneWhiteList);
    }

    @Override
    public void batchCreateWhiteList(BatchCreateWhiteListCommand cmd) {
        if (StringUtils.isBlank(cmd.getPhoneNumbers())) {
            throw new IllegalArgumentException("Illegal argument phoneNumbers null.");
        }

        List<PhoneWhiteList> list = new ArrayList<>();
        List<String> allPhoneNumbers = this.whiteListProvider.listAllWhiteList(cmd.getNamespaceId());
        String[] phoneNumbers = cmd.getPhoneNumbers().split(",");
        StringBuffer existsPhones = new StringBuffer();
        for (String phone :phoneNumbers) {
            if (StringUtils.isBlank(phone)) {
                LOGGER.error("PhoneNumber cannot be null.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "PhoneNumber cannot be null.");
            }
            if (null == cmd.getNamespaceId()) {
                LOGGER.error("namespaceId cannot be null.");
                throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                        "namespaceId cannot be null.");
            }
            if (allPhoneNumbers.contains(phone)) {
                existsPhones.append(phone).append(",");
                continue;
            }
            User user = UserContext.current().getUser();
            PhoneWhiteList phoneWhiteList = new PhoneWhiteList();
            phoneWhiteList.setPhoneNumber(phone);
            phoneWhiteList.setNamespaceId(cmd.getNamespaceId());
            phoneWhiteList.setCreatorUid(user.getId());
            phoneWhiteList.setCreateTime(new Timestamp(System.currentTimeMillis()));
            list.add(phoneWhiteList);
        }
        if (existsPhones.length() > 0) {
            LOGGER.error("PhoneNumber {} is exists.",existsPhones.toString());
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, WhiteListServiceErrorCode.ERROR_WHITELIST_PHONE_IS_EXISTS,
                    existsPhones.toString());
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

        PhoneWhiteList checkPhoneIsExists = this.whiteListProvider.checkPhoneIsExists(phoneWhiteList.getNamespaceId(), cmd.getPhoneNumber());

        if (null != checkPhoneIsExists) {
            LOGGER.error("PhoneNumber is exists.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, WhiteListServiceErrorCode.ERROR_WHITELIST_PHONE_IS_EXISTS,
                    "PhoneNumber is exists.");
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
        PhoneWhiteList phoneWhiteList = this.whiteListProvider.getWhiteList(cmd.getId());
        WhiteListDTO whiteListDTO = ConvertHelper.convert(phoneWhiteList, WhiteListDTO.class);
        if (null != phoneWhiteList && null != phoneWhiteList.getCreateTime()) {
            whiteListDTO.setCreateTime(phoneWhiteList.getCreateTime().toString());
        }
        return whiteListDTO;
    }

    @Override
    public ListWhiteListResponse listWhiteList(ListWhiteListCommand cmd) {
        if(null == cmd.getNamespaceId()) {
            LOGGER.error("namespaceId cannot be null.");
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "namespaceId cannot be null.");
        }
        ListWhiteListResponse response = new ListWhiteListResponse();
        cmd.setPageSize(PaginationConfigHelper.getPageSize(configurationProvider, cmd.getPageSize()));
        List<PhoneWhiteList> list = whiteListProvider.listWhiteList(cmd.getPhoneNumber(), cmd.getNamespaceId(), cmd.getPageAnchor(), cmd.getPageSize());
        int size = list.size();
        if (size >0) {
            if (size <= cmd.getPageSize()) {
                response.setNextPageAnchor(null);
            }else {
                list.remove(size-1);
                response.setNextPageAnchor(list.get(list.size() - 1).getId());
            }
            response.setDtos(list.stream().map(r -> {
                WhiteListDTO whiteListDTO = ConvertHelper.convert(r, WhiteListDTO.class);
                whiteListDTO.setCreateTime(r.getCreateTime().toString());
                return  whiteListDTO;
            }).collect(Collectors.toList()));
        }
        return response;
    }
}
