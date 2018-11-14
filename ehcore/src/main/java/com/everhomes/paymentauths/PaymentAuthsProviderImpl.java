// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.print.SiyinPrintSetting;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhEnterprisePaymentAuthsDao;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintSettingsDao;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuths;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintSettings;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthsRecord;
import com.everhomes.user.UserContext;
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
	private EhEnterprisePaymentAuthsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}
	
	private EhEnterprisePaymentAuthsDao getDao(DSLContext context) {
		return new EhEnterprisePaymentAuthsDao(context.configuration());
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
	public void createEnterprisePaymentAuths(List<EnterprisePaymentAuths> enterpriesAuths, Long appId, Long orgId) {
	    dbProvider.execute(r -> {
	    	//删除原来的设置
	    	getReadWriteContext().delete(Tables.EH_ENTERPRISE_PAYMENT_AUTHS)
			.where(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.APP_ID.eq(appId))
			.and(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.ENTERPRISE_ID.eq(orgId)).execute();
	    	//更新原来的设置
		    for (EnterprisePaymentAuths enterprisePaymentAuth : enterpriesAuths) {
		    	Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintSettings.class));
		    	enterprisePaymentAuth.setId(id);
		    	enterprisePaymentAuth.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		    	enterprisePaymentAuth.setUpdateTime(enterprisePaymentAuth.getCreateTime());
				getReadWriteDao().insert(enterprisePaymentAuth);
				DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintSettings.class, null);
		    }
		    return null;
	    });
	}
	
	@Override
	public void deleteEnterprisePaymentAuths(Long appId, Long orgId) {
	    dbProvider.execute(r -> {
	    	//删除原来的设置
	    	getReadWriteContext().delete(Tables.EH_ENTERPRISE_PAYMENT_AUTHS)
			.where(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.APP_ID.eq(appId))
			.and(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.ENTERPRISE_ID.eq(orgId)).execute();
		    return null;
	    });
	}
}
