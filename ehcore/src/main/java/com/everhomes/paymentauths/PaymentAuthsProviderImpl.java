// @formatter:off
package com.everhomes.paymentauths;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.paymentauths.EnterprisePaymentAuths;
import com.everhomes.paymentauths.PaymentAuthsProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.pojos.EhEnterprisePaymentAuths;
import com.everhomes.server.schema.tables.records.EhEnterprisePaymentAuthsRecord;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;

import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PaymentAuthsProviderImpl implements PaymentAuthsProvider {
    @Autowired
    private DbProvider dbProvider;
    
	@Override
	public EnterprisePaymentAuths findPaymentAuthByAppIdOrgId (Long appId, Long orgId){
		
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readWriteWith(EhEnterprisePaymentAuths.class));
        SelectQuery<EhEnterprisePaymentAuthsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTHS);
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.ENTERPRISE_ID.eq(orgId));
        query.addConditions(Tables.EH_ENTERPRISE_PAYMENT_AUTHS.APP_ID.eq(appId));
        return query.fetchAnyInto(EnterprisePaymentAuths.class);
	}
	
	@Override
	public List<EnterprisePaymentAuths> getPaymentAuths (Integer namespaceId) {
		DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnlyWith(EhEnterprisePaymentAuths.class));
		SelectQuery<EhEnterprisePaymentAuthsRecord> query = context.selectQuery(Tables.EH_ENTERPRISE_PAYMENT_AUTHS);
		return query.fetch().map(r -> ConvertHelper.convert(r, EnterprisePaymentAuths.class));
	}
}
