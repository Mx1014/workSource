package com.everhomes.wifi;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SelectConditionStep;
import org.jooq.SelectQuery;
import org.jooq.SelectWhereStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.pmsy.PmsyPayerStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.EhPmsyPayers;
import com.everhomes.server.schema.tables.daos.EhPmsyCommunitiesDao;
import com.everhomes.server.schema.tables.daos.EhPmsyOrderItemsDao;
import com.everhomes.server.schema.tables.daos.EhPmsyOrdersDao;
import com.everhomes.server.schema.tables.daos.EhPmsyPayersDao;
import com.everhomes.server.schema.tables.pojos.EhPmsyCommunities;
import com.everhomes.server.schema.tables.pojos.EhPmsyOrderItems;
import com.everhomes.server.schema.tables.pojos.EhPmsyOrders;
import com.everhomes.server.schema.tables.records.EhPmsyCommunitiesRecord;
import com.everhomes.server.schema.tables.records.EhPmsyOrdersRecord;
import com.everhomes.server.schema.tables.records.EhPmsyPayersRecord;
import com.everhomes.util.ConvertHelper;

@Component
public class WifiProviderImpl implements WifiProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WifiProviderImpl.class);
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired 
    private SequenceProvider sequenceProvider;
	
	/*@Override
	public List<PmsyPayer> listPmPayers(Long id,Integer namespaceId){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhPmsyPayers.class));
		SelectWhereStep<EhPmsyPayersRecord> select = context.selectFrom(Tables.EH_PMSY_PAYERS);
		
		Result<EhPmsyPayersRecord> result = select.where(Tables.EH_PMSY_PAYERS.STATUS.eq(PmsyPayerStatus.ACTIVE.getCode()))
			  .and(Tables.EH_PMSY_PAYERS.CREATOR_UID.eq(id))
			  .and(Tables.EH_PMSY_PAYERS.NAMESPACE_ID.eq(namespaceId))
			  .fetch();
		
		return result.map(r -> ConvertHelper.convert(r, PmsyPayer.class));
	}*/

}
