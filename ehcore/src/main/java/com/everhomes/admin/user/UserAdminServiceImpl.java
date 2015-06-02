package com.everhomes.admin.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.PaginationCommand;
import com.everhomes.user.User;
import com.everhomes.user.UserActivityProvider;
import com.everhomes.user.UserAdminProvider;
import com.everhomes.user.UserAdminService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;

@Component
public class UserAdminServiceImpl implements UserAdminService {
    @Autowired
    private UserAdminProvider userAdminProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Autowired
    private UserActivityProvider userActivityProvider;

    @Override
    public Tuple<Long, List<UserInfo>> listRegisterUsers(PaginationCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        User user = UserContext.current().getUser();
        List<UserInfo> userInfos = userAdminProvider.listRegisterByOrder(locator, pageNum + 1, null);
        if (CollectionUtils.isEmpty(userInfos)) {
            return new Tuple<Long, List<UserInfo>>(null,null);
        }
        Map<Long, UserInfo> cache = new HashMap<Long, UserInfo>();
        List<Long> uids = userInfos.stream().map(r -> {
            cache.put(r.getId(), r);
            r.setEmails(new ArrayList<String>());
            r.setPhones(new ArrayList<String>());
            try{
                r.setAvatarUrl(contentServerService.parserUri(r.getAvatarUri(), EntityType.USER.getCode(), user.getId()));
            }catch(Exception e){}
           

            return r.getId();
        }).collect(Collectors.toList());
        List<UserIdentifier> identifiers = userAdminProvider.listUserIdentifiers(uids);
        identifiers.forEach(identifier -> {
            UserInfo userInfo = cache.get(identifier.getOwnerUid());
            if (identifier.getIdentifierType().equals(IdentifierType.EMAIL.getCode())) {
                userInfo.getEmails().add(identifier.getIdentifierToken());
            }
        });
        Long nextAnchor = null;
        List<UserInfo> values=userInfos;
        if (userInfos.size() > pageNum) {
            values=userInfos.subList(0, userInfos.size()-1);
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserInfo>>(nextAnchor,values);
    }

    @Override
    public Tuple<Long, List<UserIdentifier>> listUserIdentifiers(PaginationCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserIdentifier> identifiers = userAdminProvider.listUserIdentifiersByOrder(locator, pageNum, null);
        Long nextAnchor = null;
        List<UserIdentifier> values = identifiers;
        if (identifiers.size() > pageNum) {
            values=identifiers.subList(1, identifiers.size());
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserIdentifier>>(nextAnchor, values);
    }

    @Override
    public Tuple<Long, List<UserInfo>> listVets(PaginationCommand cmd) {
        User user=UserContext.current().getUser();
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserIdentifier> identifiers = userAdminProvider.listVetsByOrder(locator, pageNum + 1, null);
        // cache phone number
        HashMap<Long, String> cache = new HashMap<Long, String>();
        identifiers.forEach(identifier -> {
            cache.put(identifier.getOwnerUid(), identifier.getIdentifierToken());
        });
        List<User> users = userActivityProvider.listUsers(new ArrayList<Long>(cache.keySet()));
        List<UserInfo> userInfos = users.stream().map(r -> {
            UserInfo info = ConvertHelper.convert(r, UserInfo.class);
            if (CollectionUtils.isEmpty(info.getPhones())) {
                info.setPhones(new ArrayList<String>());
            }
            info.getPhones().add(cache.get(r.getId()));
            info.setAvatarUri(r.getAvatar());
            try{
                info.setAvatarUrl(contentServerService.parserUri(r.getAvatar(), EntityType.USER.getCode(), user.getId()));
            }catch(Exception e){}
            return info;
        }).collect(Collectors.toList());
        List<UserInfo> values = userInfos;
        Long nextAnchor=null;
        if (identifiers.size() > pageNum) {
            values=userInfos.subList(1, identifiers.size());
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserInfo>>(nextAnchor, values);
    }

    @Override
    public Tuple<Long, List<UserIdentifier>> listVerifyCode(PaginationCommand cmd) {
        CrossShardListingLocator locator = new CrossShardListingLocator();
        if (cmd.getAnchor() != null) {
            locator.setAnchor(cmd.getAnchor());
        }
        int pageNum = configurationProvider.getIntValue("pagination.page.size", AppConstants.PAGINATION_DEFAULT_SIZE);
        List<UserIdentifier> userIdentifiers = userAdminProvider.listAllVerifyCode(locator, pageNum + 1, null);
        Long nextAnchor = null;
        List<UserIdentifier> values = userIdentifiers;
        if (userIdentifiers.size() > pageNum) {
            values=userIdentifiers.subList(1, userIdentifiers.size());
            nextAnchor = locator.getAnchor();
        }
        return new Tuple<Long, List<UserIdentifier>>(nextAnchor, values);
    }

}
