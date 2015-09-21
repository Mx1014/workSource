package com.everhomes.techpark.company;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGroupContactsDao;
import com.everhomes.server.schema.tables.pojos.EhGroupContacts;
import com.everhomes.server.schema.tables.pojos.EhPunchLogs;
import com.everhomes.techpark.company.GroupContact;
import com.everhomes.techpark.company.GroupContactProvider;
import com.everhomes.techpark.company.OwnerType;
import com.everhomes.util.ConvertHelper;

import freemarker.template.utility.StringUtil;

@Component
public class CompanyProviderImpl implements GroupContactProvider{
	@Autowired
	private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    
	@Override
	public GroupContact findGroupContactByToken(String contactToken) {

		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_GROUP_CONTACTS.CONTACT_TOKEN.eq(contactToken);
		List<GroupContact> list = context.select().from(Tables.EH_GROUP_CONTACTS)
				.where(condition)
				.fetch().map(r -> {
					return ConvertHelper.convert(r, GroupContact.class);
				});
		if(list == null || list.isEmpty()) return null;
		return list.get(0);
	}

	@Override
	public void updateGroupContact(GroupContact gContact) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		dao.update(ConvertHelper.convert(gContact, EhGroupContacts.class));
		DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhGroupContacts.class, gContact.getId());
	}

	@Override
	public void createGroupContact(GroupContact gContact) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite()); 
		long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhGroupContacts.class));
		gContact.setId(id);
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		dao.insert(ConvertHelper.convert(gContact, EhGroupContacts.class));
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhGroupContacts.class, null);
	}

	@Override
	public void deleteGroupContactById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhGroupContacts.class, id);
	}

	@Override
	public GroupContact findGroupContactById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		EhGroupContacts ehObj = dao.fetchOneById(id);
		if(ehObj != null)
			return ConvertHelper.convert(ehObj, GroupContact.class);
		return null;
	}

	@Override
	public List<GroupContact> listGroupContactsByKeword(Long ownerId,String keyword, Integer pageSize, Long offset) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_GROUP_CONTACTS.OWNER_TYPE.eq(OwnerType.COMPANY.getCode()).and(Tables.EH_GROUP_CONTACTS.OWNER_ID.eq(ownerId));
		
		if(!StringUtils.isEmpty(keyword))	condition = condition.and(Tables.EH_GROUP_CONTACTS.CONTACT_NAME.like("%"+keyword+"%"));
		List<GroupContact> list = context.select().from(Tables.EH_GROUP_CONTACTS)
									.where(condition)
									.limit(pageSize).offset(offset.intValue())
									.fetch().map(r -> {
										return ConvertHelper.convert(r, GroupContact.class);
									});
		
		if(list == null || list.isEmpty()) return null;
		return list;
	}

}
