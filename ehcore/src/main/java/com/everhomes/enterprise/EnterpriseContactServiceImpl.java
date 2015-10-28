package com.everhomes.enterprise;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserInfo;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;

@Component
public class EnterpriseContactServiceImpl implements EnterpriseContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnterpriseContactServiceImpl.class);
    
    @Autowired
    UserService userService;
    
    @Autowired
    EnterpriseService enterpriseService;
    
    @Autowired
    private EnterpriseContactProvider enterpriseContactProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Override
    public List<EnterpriseContact> listContacts() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createContact(EnterpriseContact contact) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateContact(EnterpriseContact contact) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteContactById(Long contactId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<EnterpriseContactGroupMember> listMembersByGroupId(Long groupId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createGroup(EnterpriseContactGroup group) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateGroup(EnterpriseContactGroup group) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteByGroupId(Long groupId) {
        // TODO Auto-generated method stub
        
    }

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Enterprise> queryEnterpriseByPhone(String phone) {
        return this.enterpriseService.listEnterpriseByPhone(phone);
    }

    @Override
    public void bindUserToContact(User user, EnterpriseContact contact) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void approveUserToContact(User user, EnterpriseContact contact) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rejectUserFromContact(EnterpriseContact contact) {
        // TODO Auto-generated method stub
        
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
        this.createContact(contact);
        
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
        //assert contact is from db and status is approve
        contact.setStatus(EnterpriseContactStatus.Approved.getCode());
        this.enterpriseContactProvider.updateContact(contact);
        
        //Set group for this contact
        String applyGroup = contact.getApplyGroup();
        if(applyGroup != null && !applyGroup.isEmpty()) {
            EnterpriseContactGroup group = this.enterpriseContactProvider.getContactGroupByName(contact.getEnterpriseId(), applyGroup);
            approveContactToGroup(contact, group);
        }
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
    
    public EnterpriseContactDetail getContactByPhone(String phone) {
        EnterpriseContactDetail detail = null;
        EnterpriseContactEntry entry = this.enterpriseContactProvider.getEnterpriseContactEntryByPhone(phone);
        if(entry != null) {
            EnterpriseContact contact = this.enterpriseContactProvider.getContactById(entry.getContactId());
            if(contact != null) {
                detail = ConvertHelper.convert(contact, EnterpriseContactDetail.class);
                detail.setPhone(phone);
            }
        }
        
        return detail;
    }
}
