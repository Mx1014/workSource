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
import com.everhomes.server.schema.tables.pojos.EhCompanyPhoneList;
import com.everhomes.util.ConvertHelper;

@Component
public class CompanyProviderImpl implements CompanyProvider{
	@Autowired
	private DbProvider dbProvider;

	@Override
	public CompanyPhoneList findComPhoneListByPhone(String telephone) {
		
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
		List<CompanyPhoneList> list = context.select().from(Tables.EH_COMPANY_PHONE_LIST)
				.where(Tables.EH_COMPANY_PHONE_LIST.TELEPHONE.eq(telephone))
				.fetch().map(r -> {
					return ConvertHelper.convert(r, CompanyPhoneList.class);
				});
		if(list == null || list.isEmpty()) return null;
		return list.get(0);
	}

	@Override
	public void updateComPhoneList(CompanyPhoneList comPhone) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		
		DaoHelper.publishDaoAction(DaoAction.MODIFY,  EhCompanyPhoneList.class, comPhone.getId());
	}

	@Override
	public void createComPhoneList(CompanyPhoneList comPhone) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWrite());
		
		DaoHelper.publishDaoAction(DaoAction.CREATE,  EhCompanyPhoneList.class, null);
	}

	@Override
	public void deleteComPhoneListById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CompanyPhoneList findCompanyPhoneListById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
