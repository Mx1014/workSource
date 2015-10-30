package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.app.AppConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessageBodyType;
import com.everhomes.messaging.MessageChannel;
import com.everhomes.messaging.MessageDTO;
import com.everhomes.messaging.MessageMetaConstant;
import com.everhomes.messaging.MessagingConstants;
import com.everhomes.messaging.MessagingService;
import com.everhomes.messaging.MetaObjectType;
import com.everhomes.messaging.QuestionMetaObject;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.MessageChannelType;
import com.everhomes.user.User;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserProvider;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

@Component
public class EnterpriseContactServiceImpl implements EnterpriseContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseContactServiceImpl.class);
    
    @Autowired
    private UserProvider userProvider;
    
    @Autowired
    EnterpriseService enterpriseService;
    
    @Autowired
    private EnterpriseContactProvider enterpriseContactProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    MessagingService messagingService;
    
    @Autowired
    LocaleTemplateService localeTemplateService;

    @Override
    public void addContactGroupMember(EnterpriseContactGroup group, EnterpriseContactGroupMember member) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeContactGroupMember(EnterpriseContactGroup group, EnterpriseContactGroupMember member) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<EnterpriseContact> queryContactByPhone(String phone) {
        return this.enterpriseContactProvider.queryEnterpriseContactByPhone(phone);
    }

    @Override
    public List<Enterprise> queryEnterpriseByPhone(String phone) {
        return this.enterpriseService.listEnterpriseByPhone(phone);
    }

    @Override
    public void processUserForContact(UserIdentifier identifier) {
        //UserInfo user = userService.getUserSnapshotInfoWithPhone(identifier.getOwnerUid());
        User user = userProvider.findUserById(identifier.getOwnerUid());
        List<Enterprise> enterprises = this.enterpriseService.listEnterpriseByPhone(identifier.getIdentifierToken());
        for(Enterprise en : enterprises) {
            //Try to create a contact for this enterprise
            EnterpriseContact contact = this.enterpriseContactProvider.queryContactByUserId(en.getId(), identifier.getOwnerUid());
            if(null == contact) {
                //create new contact for it
                contact = new EnterpriseContact();
                contact.setAvatar(user.getAvatar());
                contact.setEnterpriseId(en.getId());
                contact.setName(user.getNickName());
                contact.setNickName(user.getNickName());
                contact.setUserId(user.getId());
                
                //auto approved
                contact.setStatus(EnterpriseContactStatus.Approved.getCode());
                this.enterpriseContactProvider.createContact(contact);
                
                //create a entry for it, but not for all  user identifier
                EnterpriseContactEntry entry = new EnterpriseContactEntry();
                entry.setContactId(contact.getId());
                entry.setCreatorUid(0l);
                entry.setEnterpriseId(contact.getEnterpriseId());
                entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
                entry.setEntryValue(identifier.getIdentifierToken());
                this.enterpriseContactProvider.createContactEntry(entry);
                
                //TODO send message for this contact
                //sendMessageForContactApproved(contact);
            }
        }
        
    }

    /**
     * 同意某用户绑定到某企业通讯录，此接口应该用不到
     */
    @Override
    public void approveUserToContact(User user, EnterpriseContact contact) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 拒绝用户申请某企业的通讯录
     */
    @Override
    public void rejectUserFromContact(EnterpriseContact contact) {
        //设置为删除
        contact.setStatus(EnterpriseContactStatus.Inactive.getCode());
        this.enterpriseContactProvider.updateContact(contact);
        
        //TODO 发消息
    }

    /**
     * 申请加入企业
     * @param contact
     */
    @Override
    public EnterpriseContact applyForContact(CreateContactByUserIdCommand cmd) {
        //Check exists
        EnterpriseContact existContact = this.enterpriseContactProvider.queryContactByUserId(cmd.getEnterpriseId(), cmd.getUserId());
        if(null != existContact) {
            //Should response error hear
            //contact.setId(existContact.getId());
            return existContact;
        }
        
        //TODO check group?
        
        EnterpriseContact contact = ConvertHelper.convert(cmd, EnterpriseContact.class);
        if (null == contact) {
            return null;
        }
        
        //Create it
        contact.setStatus(EnterpriseContactStatus.Approving.getCode());
        this.enterpriseContactProvider.createContact(contact);
        
        //Create contact entry from userinfo
        UserInfo userInfo = this.userService.getUserSnapshotInfoWithPhone(contact.getUserId());
        List<String> phones = userInfo.getPhones();
        for(String phone : phones) {
            //TODO for email
            EnterpriseContactEntry entry = new EnterpriseContactEntry();
            entry.setContactId(contact.getId());
            entry.setCreatorUid(0l);
            entry.setEnterpriseId(contact.getEnterpriseId());
            entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
            entry.setEntryValue(phone);
            try {
                this.enterpriseContactProvider.createContactEntry(entry);   
            } catch(Exception ex) {
                LOGGER.info("create phone entry error contactId " + contact.getId() + " phone " + phone);
            }
        }
        return contact;
    }
    
    /**
     * 批准用户加入企业
     */
    public void approveContact(EnterpriseContact contact) {        
        //TODO 发消息
        
        //assert contact is from db and status is approve
        contact.setStatus(EnterpriseContactStatus.Approved.getCode());
        this.enterpriseContactProvider.updateContact(contact);
        
        //Set group for this contact
        String applyGroup = contact.getApplyGroup();
        if(applyGroup != null && !applyGroup.isEmpty()) {
            EnterpriseContactGroup group = this.enterpriseContactProvider.getContactGroupByName(contact.getEnterpriseId(), applyGroup);
            approveContactToGroup(contact, group);
        }
        
        //sendMessageForContactApproved(contact);
    }
    
    /**
     * 将contact加入组
     * @param contact
     * @param group
     */
    public void approveContactToGroup(EnterpriseContact contact, EnterpriseContactGroup group) {
        EnterpriseContactGroupMember member = this.enterpriseContactProvider.getContactGroupMemberByContactId(contact.getEnterpriseId()
                , contact.getId(), group.getId());
        if(null == member) {
            member = new EnterpriseContactGroupMember(); 
            member.setContactGroupId(group.getId());
            member.setContactId(contact.getId());
            member.setEnterpriseId(contact.getEnterpriseId());
            //Default, set for approved
            member.setContactStatus(EnterpriseGroupMemberStatus.Approved.getCode());
            this.enterpriseContactProvider.createContactGroupMember(member);
            
            return;
        }
        
        if(member.getContactStatus() == EnterpriseGroupMemberStatus.Approving.getCode()) {
            member.setContactStatus(EnterpriseGroupMemberStatus.Approved.getCode());
            this.enterpriseContactProvider.updateContactGroupMember(member);
        }
        
    }
    
    /**
     * 显示所有通讯录列表
     * @param locator
     * @param enterpriseId
     * @param count
     * @return
     */
    @Override
    public List<EnterpriseContactDetail> listContactByEnterpriseId(ListingLocator locator, Long enterpriseId, Integer pageSize) {
        int count = PaginationConfigHelper.getPageSize(configProvider, pageSize);
        List<EnterpriseContact> contacts = this.enterpriseContactProvider.listContactByEnterpriseId(locator, enterpriseId, count);
        List<EnterpriseContactDetail> details = new ArrayList<EnterpriseContactDetail>();
        for(EnterpriseContact contact : contacts) {
            EnterpriseContactDetail detail = ConvertHelper.convert(contact, EnterpriseContactDetail.class);
            EnterpriseContactGroupMember member = this.enterpriseContactProvider.getContactGroupMemberByContactId(enterpriseId, contact.getId());
            if (member != null) {
                EnterpriseContactGroup group = this.enterpriseContactProvider.getContactGroupById(member.getContactGroupId());
                if(group != null) {
                    detail.setGroupName(group.getName());
                }
            }
            
            List<EnterpriseContactEntry> entries = this.enterpriseContactProvider.queryContactEntryByContactId(contact);
            if(entries != null && entries.size() > 0) {
                detail.setPhone(entries.get(0).getEntryValue());
            }
            
            details.add(detail);
            
        }
        
        return details;
    }
    
    /**
     * 同一个手机好可能在多个企业，可以搜索某一个手机号属于的企业，在查询企业下的具体手机号。
     * @param phone
     * @return
     */
    public EnterpriseContactDetail getContactByPhone(Long enterpriseId, String phone) {
        EnterpriseContactDetail detail = null;
        EnterpriseContactEntry entry = this.enterpriseContactProvider.getEnterpriseContactEntryByPhone(enterpriseId, phone);
        if(entry != null) {
            EnterpriseContact contact = this.enterpriseContactProvider.getContactById(entry.getContactId());
            if(contact != null) {
                detail = ConvertHelper.convert(contact, EnterpriseContactDetail.class);
                detail.setPhone(phone);
            }
        }
        
        return detail;
    }
    
    private void sendEnterpriseNotification(Long enterpriseId, List<Long> includeList, List<Long> excludeList, 
            String message, MetaObjectType metaObjectType, QuestionMetaObject metaObject) {
        if(message != null && message.length() != 0) {
            String channelType = MessageChannelType.GROUP.getCode();
            String channelToken = String.valueOf(enterpriseId);
            MessageDTO messageDto = new MessageDTO();
            messageDto.setAppId(AppConstants.APPID_MESSAGING);
            messageDto.setSenderUid(User.SYSTEM_UID);
            messageDto.setChannels(new MessageChannel(channelType, channelToken));
            messageDto.setBodyType(MessageBodyType.NOTIFY.getCode());
            messageDto.setBody(message);
            messageDto.setMetaAppId(AppConstants.APPID_ENTERPRISE);
            if(includeList != null && includeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.INCLUDE, StringHelper.toJsonString(includeList));
            }
            if(excludeList != null && excludeList.size() > 0) {
                messageDto.getMeta().put(MessageMetaConstant.EXCLUDE, StringHelper.toJsonString(excludeList));
            }
            if(metaObjectType != null && metaObject != null) {
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT_TYPE, metaObjectType.getCode());
                messageDto.getMeta().put(MessageMetaConstant.META_OBJECT, StringHelper.toJsonString(metaObject));
            }
            messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, channelType, 
                channelToken, messageDto, MessagingConstants.MSG_FLAG_STORED.getCode());
        }
    }
}
