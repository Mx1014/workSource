package com.everhomes.techpark.punch.company;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhGroupContactsDao;
import com.everhomes.server.schema.tables.pojos.EhGroupContacts;
import com.everhomes.util.ConvertHelper;

@Component
public class CompanyProviderImpl implements CompanyProvider{
	@Autowired
	private DbProvider dbProvider;

	@Override
	public GroupContact findComPhoneListByPhone(String telephone) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<GroupContact> list = context.select().from(Tables.EH_GROUP_CONTACTS)
				.where(Tables.EH_GROUP_CONTACTS.CONTACT_TOKEN.eq(telephone))
				.fetch().map(r -> {
					return ConvertHelper.convert(r, GroupContact.class);
				});
		if(list == null || list.isEmpty()) return null;
		return list.get(0);
	}

	@Override
	public void updateComPhoneList(GroupContact comPhone) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		dao.update(ConvertHelper.convert(comPhone, EhGroupContacts.class));
		DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhGroupContacts.class, comPhone.getId());
	}

	@Override
	public void createComPhoneList(GroupContact comPhone) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		dao.insert(ConvertHelper.convert(comPhone, EhGroupContacts.class));
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhGroupContacts.class, null);
	}

	@Override
	public void deleteComPhoneListById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		dao.deleteById(id);
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhGroupContacts.class, id);
	}

	@Override
	public GroupContact findCompanyPhoneListById(Long id) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		EhGroupContactsDao dao = new EhGroupContactsDao(context.configuration());
		EhGroupContacts ehObj = dao.fetchOneById(id);
		if(ehObj != null)
			return ConvertHelper.convert(ehObj, GroupContact.class);
		return null;
	}

}
