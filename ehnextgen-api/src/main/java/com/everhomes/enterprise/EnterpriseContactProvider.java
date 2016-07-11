package com.everhomes.enterprise;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;

public interface EnterpriseContactProvider {
    public Long createContact(EnterpriseContact contact);
    public void updateContact(EnterpriseContact contact);
    public void deleteContactById(EnterpriseContact contact);
    public EnterpriseContact getContactById(Long id);
    public EnterpriseContact queryContactByUserId(Long enterpriseId, Long userId);
    public List<EnterpriseContact> queryContactByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback);
    public List<EnterpriseContact> queryContacts(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    public void createContactEntry(EnterpriseContactEntry entry);
    public void updateContactEntry(EnterpriseContactEntry entry);
    public void deleteContactEntry(EnterpriseContactEntry entry);
    public EnterpriseContactEntry getContactEntryById(Long id);
    List<EnterpriseContactEntry> queryEnterpriseContactEntries(byte entryType, String entryValue);
    public List<EnterpriseContactEntry> queryContactEntryByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback);
    public List<EnterpriseContactEntry> queryContactEntries(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    public void createContactGroup(EnterpriseContactGroup group);
    public void updateContactGroup(EnterpriseContactGroup group);
    public void deleteContactGroup(EnterpriseContactGroup group);
    public EnterpriseContactGroup getContactGroupById(Long id);
    public List<EnterpriseContactGroup> queryContactGroupByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback);
    public List<EnterpriseContactGroup> queryContactGroups(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    public void createContactGroupMember(EnterpriseContactGroupMember member);
    public void updateContactGroupMember(EnterpriseContactGroupMember member);
    public void deleteContactGroupMember(EnterpriseContactGroupMember member);
    public EnterpriseContactGroupMember getContactGroupMemberById(Long id);
    public List<EnterpriseContactGroupMember> queryContactGroupMemberByEnterpriseId(ListingLocator locator, Long enterpriseId
            , int count, ListingQueryBuilderCallback queryBuilderCallback);
    public List<EnterpriseContactGroupMember> queryContactGroupMembers(CrossShardListingLocator locator, int count, 
            ListingQueryBuilderCallback queryBuilderCallback);
    public EnterpriseContactGroup getContactGroupByName(Long enterpriseId, String group);
    public EnterpriseContactGroupMember getContactGroupMemberByContactId(Long enterpriseId, Long contactId, Long groupId);
    List<EnterpriseContact> listContactByEnterpriseId(ListingLocator locator, Long enterpriseId, int count,String keyWord);
    EnterpriseContactGroupMember getContactGroupMemberByContactId(Long enterpriseId, Long contactId);
    List<EnterpriseContactEntry> queryContactEntryByContactId(EnterpriseContact contact);
    EnterpriseContactEntry getEnterpriseContactEntryByPhone(Long enterpriseId, String value);
    List<EnterpriseContact> queryEnterpriseContactByPhone(String phone);
	List<EnterpriseContactEntry> queryContactEntryByContactId(
			ListingLocator locator, Integer count, EnterpriseContact contact);
	List<EnterpriseContactEntry> queryContactEntryByContactId(
			EnterpriseContact contact, Byte contactType); 
	List<EnterpriseContact> queryEnterpriseContactByKeyword(String keyword);
	void iterateEnterpriseContacts(int count, ListingQueryBuilderCallback queryBuilderCallback, 
        IterateEnterpriseContactCallback callback);
	public List<EnterpriseContact> listContactRequestByEnterpriseId(
			ListingLocator locator, Long enterpriseId, int count,String keyWord);
	public EnterpriseContact queryContactById(Long contactId);
	public List<EnterpriseContactEntry> queryContactEntryByEnterpriseIdAndPhone(
			ListingLocator locator, Long enterpriseId, String phoneString,
			ListingQueryBuilderCallback queryBuilderCallback);
	public List<EnterpriseContactGroupMember> queryContactGroupMemberByEnterpriseIdAndContactId(
			ListingLocator locator,Long enterpriseId, Long contactId, ListingQueryBuilderCallback queryBuilderCallback);

	public List<EnterpriseContactGroup> listContactGroupsByEnterpriseId(
			ListingLocator locator, Long enterpriseId, int count);
	public EnterpriseContactGroup queryContactGroupById(Long Id);
	public List<EnterpriseContactGroup> queryContactGroupByPath(
			Long enterpriseId, Long groupId);
	
	List<Long> deleteContactByEnterpriseId(Long enterpriseId);
	void deleteContactEntryByContactId(List<Long> contactIds);
	
	EnterpriseContact queryEnterpriseContactor(Long enterpriseId); 
	List<EnterpriseContactGroupMember> listContactGroupMemberByContactGroupId(
			Long enterpriseId, Long groupId);
	List<EnterpriseContact> queryEnterpriseContactByKeywordAndGroupId(
			Long enterpriseId, String keyword, Long enterpriseGroupId); 
	List<EnterpriseContact> queryContactByEnterpriseId(Long enterpriseId, String keyword);
	List<EnterpriseContact> queryContact(CrossShardListingLocator locator, int count);
	EnterpriseContact queryContactByUserId(Long userId);
	
	List<Long> queryUserIds();
}
