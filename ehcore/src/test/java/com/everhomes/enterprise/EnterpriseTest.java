package com.everhomes.enterprise;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.enterprise.EnterpriseCommunityMapType;
import com.everhomes.rest.enterprise.EnterpriseCommunityOperatorType;
import com.everhomes.rest.enterprise.EnterpriseCommunityStatus;
import com.everhomes.rest.enterprise.EnterpriseContactEntryType;
import com.everhomes.rest.enterprise.EnterpriseGroupMemberStatus;
import com.everhomes.search.EnterpriseSearcher;
import com.everhomes.user.base.LoginAuthTestCase;
import com.everhomes.util.ConvertHelper;

public class EnterpriseTest extends LoginAuthTestCase {
    @Autowired
    EnterpriseProvider enterpriseProvider;
    
    @Autowired
    private CommunityProvider communityProvider;
    
    @Autowired
    private EnterpriseContactProvider enterpriseContactProvider;
    
    @Autowired
    private EnterpriseSearcher enterpriseSearcher;
    
    @Configuration
    @ComponentScan(basePackages = {
        "com.everhomes"
    })
    @EnableAutoConfiguration(exclude={
            DataSourceAutoConfiguration.class, 
            HibernateJpaAutoConfiguration.class,
        })
    static class ContextConfiguration {
    }
    
    @Before
    public void setUp() throws Exception {
        
    }
    
    @After
    public void tearDown() {
        
    }
    
    //@Test
    public void testEnterpriseCommunity() {
        Community ec1 = communityProvider.findCommunityById(24206890946790401l);
        Assert.assertTrue(ec1 != null);
        
        EnterpriseCommunity ec2 = ConvertHelper.convert(ec1, EnterpriseCommunity.class);
        ec2.setId(0l);
        enterpriseProvider.createEnterpriseCommunity(1l, ec2);
        Assert.assertTrue(ec2.getId() > 0);
        
    }
    
    //@Test
    public void testEnterprise() {
        Long creatorUid = 1l;
        Enterprise enterprise = new Enterprise();
        enterprise.setName("永佳天成科技发展有限公司");
        enterprise.setDescription("仅仅为测试需要");
        enterprise.setCreatorUid(creatorUid);
        enterpriseProvider.createEnterprise(enterprise);
        Assert.assertTrue(enterprise.getId() > 0);
        
        Enterprise e2 = this.enterpriseProvider.getEnterpriseById(enterprise.getId());
        Assert.assertTrue(e2 != null);
        
        Long communityId = 24210150826969740l;
        EnterpriseCommunityMap ep = new EnterpriseCommunityMap();
        ep.setCommunityId(communityId);
        ep.setMemberType(EnterpriseCommunityMapType.Enterprise.getCode());
        ep.setMemberId(e2.getId());
        ep.setMemberStatus(EnterpriseCommunityStatus.Approving.getCode());
        ep.setCreatorUid(creatorUid);
        ep.setOperationType(EnterpriseCommunityOperatorType.RequestToJoin.getCode());
        
        this.enterpriseProvider.createEnterpriseCommunityMap(ep);
        Assert.assertTrue(ep.getId() > 0);
        
        int count = 100;
        CrossShardListingLocator l1 = new CrossShardListingLocator();
        List<Enterprise> ents = this.enterpriseProvider.queryEnterprises(l1, count, null, null);
        Assert.assertTrue(ents.size() > 0);
        
        CrossShardListingLocator l2 = new CrossShardListingLocator();
        List<EnterpriseCommunityMap> ents2 = this.enterpriseProvider.queryEnterpriseCommunityMap(l2, 100, null);
        Assert.assertTrue(ents2.size() > 0);
        
        ep.setIntegralTag1(333l);
        this.enterpriseProvider.updateEnterpriseCommunityMap(ep);
        EnterpriseCommunityMap ep2 = this.enterpriseProvider.getEnterpriseCommunityMapById(ep.getId());
        Assert.assertTrue(ep2.getIntegralTag1() == 333l);
        
        //this.enterpriseProvider.deleteEnterpriseById(e2.getId());
        //Enterprise e3 = this.enterpriseProvider.getEnterpriseById(e2.getId());
        //Assert.assertTrue(e3 == null);
    }
    
    //@Test
    public void testContact() {
        Long enterpriseId = 125565l;
        EnterpriseContact contact = new EnterpriseContact();
        contact.setAvatar("hello");
        Long creatorUid = 1l;
        contact.setCreatorUid(creatorUid);
        contact.setEnterpriseId(enterpriseId);
        contact.setName("Your name");
        contact.setNickName("Your nick name");
        this.enterpriseContactProvider.createContact(contact); 
        Assert.assertTrue(contact.getId() > 0);
        
        int count = 100;
        
        CrossShardListingLocator l1 = new CrossShardListingLocator();
        List<EnterpriseContact> contacts = this.enterpriseContactProvider.queryContacts(l1, count, null);
        Assert.assertTrue(contacts.size() > 0);
        
        ListingLocator l2 = new ListingLocator();
        List<EnterpriseContact> contacts2 = this.enterpriseContactProvider.queryContactByEnterpriseId(l2, enterpriseId, count, null);
        Assert.assertTrue(contacts2.size() > 0);
        //Assert.assertTrue(contacts2.get(0).getId() == contact.getId());
        
        EnterpriseContactEntry entry = new EnterpriseContactEntry();
        entry.setContactId(contact.getId());
        entry.setCreatorUid(creatorUid);
        entry.setEnterpriseId(contact.getEnterpriseId());
        entry.setEntryType(EnterpriseContactEntryType.Mobile.getCode());
        entry.setEntryValue("1234567890");
        this.enterpriseContactProvider.createContactEntry(entry);
        
        CrossShardListingLocator l3 = new CrossShardListingLocator();
        List<EnterpriseContactEntry> entries = this.enterpriseContactProvider.queryContactEntries(l3, count, null);
        Assert.assertTrue(entries.size() > 0);
        
        EnterpriseContactGroup group = new EnterpriseContactGroup();
        group.setCreatorUid(creatorUid);
        group.setEnterpriseId(enterpriseId);
        //group.setIntegralTag1(contact.getId());//TODO for custom field
        group.setName("研发部");
        group.setPath("/");
        this.enterpriseContactProvider.createContactGroup(group);
        
        CrossShardListingLocator l4 = new CrossShardListingLocator();
        List<EnterpriseContactGroup> groups = this.enterpriseContactProvider.queryContactGroups(l4, count, null);
        Assert.assertTrue(groups.size() > 0);
        
        EnterpriseContactGroupMember member = new EnterpriseContactGroupMember(); 
        member.setContactGroupId(group.getId());
        member.setContactId(contact.getId());
        member.setEnterpriseId(enterpriseId);
        member.setContactStatus(EnterpriseGroupMemberStatus.WAITING_FOR_APPROVAL.getCode());
        this.enterpriseContactProvider.createContactGroupMember(member);
        //member.setIntegralTag1(integralTag1);TODO Member type
        Assert.assertTrue(member.getId() > 0);
        
        CrossShardListingLocator l5 = new CrossShardListingLocator();
        List<EnterpriseContactGroupMember> members = this.enterpriseContactProvider.queryContactGroupMembers(l5, count, null);
        Assert.assertTrue(member.getId() > 0);
    }
    
    //@Test
    public void testJoin() {
        List<Enterprise> enterprises = this.enterpriseProvider.queryEnterpriseByPhone("1234567890");
        Assert.assertTrue(enterprises.size() > 0);
    }
    
    @Test
    public void testEnterpriseSearcher() {
        enterpriseSearcher.syncFromDb();
    }
}
