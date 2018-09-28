// @formatter:off
package com.everhomes.paymentAuths;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.ServiceModuleSceneType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.portal.ServiceModuleAppStatus;
import com.everhomes.rest.servicemoduleapp.OrganizationAppStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAppsDao;
import com.everhomes.server.schema.tables.pojos.EhServiceModuleApps;
import com.everhomes.server.schema.tables.records.EhOrganizationAppsRecord;
import com.everhomes.server.schema.tables.records.EhServiceModuleAppsRecord;
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
	@Override
	public Long findPaymentAuthByAppIdOrgId (String appId, Long orgId){
		return null;
	}
}
