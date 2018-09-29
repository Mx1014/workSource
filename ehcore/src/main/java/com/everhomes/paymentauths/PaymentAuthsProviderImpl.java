// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuths;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentAuthsProviderImpl implements PaymentAuthsProvider {
    @Autowired
    private DbProvider dbProvider;
    
	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}
	
	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}

	@Autowired
    private SequenceProvider sequenceProvider;
	
	@Override
	public EnterprisePaymentAuths findPaymentAuth (Long appId, Long orgId, Long sourceId){
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterprisePaymentAuths.class));
        SelectQuery<EhEnterprisePaymentAuthsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTHS);
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.ENTERPRISE_ID.eq(orgId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.APP_ID.eq(appId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.SOURCE_ID.eq(sourceId));
        return query.fetchAnyInto(EnterprisePaymentAuths.class);
	}
	
	@Override
	public List<EnterprisePaymentAuths> getPaymentAuths (Integer namespaceId, Long orgId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEnterprisePaymentAuths.class));
		SelectQuery<EhEnterprisePaymentAuthsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTHS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.ENTERPRISE_ID.eq(orgId));
		return query.fetch().map(r -> ConvertHelper.convert(r, EnterprisePaymentAuths.class));
	}
	
    @Override
    public void deleteEnterprisePaymentAuths(Long appId, Long orgId){
		DeleteQuery query = getReadWriteContext().deleteQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTHS);
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.APP_ID.eq(appId));
		query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.ENTERPRISE_ID.eq(orgId));
		query.execute();
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceModuleApps.class, null);
    }
    
	@Override
	public void createEnterprisePaymentAuths(List<EnterprisePaymentAuths> enterpriesAuths) {
		if(enterpriesAuths.size() == 0){
			return;
		}
		/**
		 * 有id使用原来的id，没有则生成新的
		 */
		Long id = sequenceProvider.getNextSequenceBlock(NameMapper.getSequenceDomainFromTablePojo(EhEnterprisePaymentAuths.class), (long)enterpriesAuths.size() + 1);
		List<EhEnterprisePaymentAuths> auths = new ArrayList<>();
		for (EnterprisePaymentAuths enterpriesAuth: enterpriesAuths) {
			if(enterpriesAuth.getId() == null){
				id ++;
				enterpriesAuth.setId(id);
			}


			enterpriesAuth.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			enterpriesAuth.setUpdateTime(enterpriesAuth.getCreateTime());
			auths.add(ConvertHelper.convert(enterpriesAuth, EhEnterprisePaymentAuths.class));
		}
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhEnterprisePaymentAuthsDao dao = new EhEnterprisePaymentAuthsDao(context.configuration());
		dao.insert(auths);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhEnterprisePaymentAuths.class, null);
	}
}
