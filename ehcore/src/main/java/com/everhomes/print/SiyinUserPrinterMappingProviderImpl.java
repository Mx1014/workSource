// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

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
import com.everhomes.server.schema.tables.daos.EhSiyinUserPrinterMappingsDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinUserPrinterMappings;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinUserPrinterMappingProviderImpl implements SiyinUserPrinterMappingProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinUserPrinterMapping(SiyinUserPrinterMapping siyinUserPrinterMapping) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinUserPrinterMappings.class));
		siyinUserPrinterMapping.setId(id);
		siyinUserPrinterMapping.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinUserPrinterMapping.setCreatorUid(UserContext.current().getUser().getId());
		siyinUserPrinterMapping.setOperateTime(siyinUserPrinterMapping.getCreateTime());
		siyinUserPrinterMapping.setOperatorUid(siyinUserPrinterMapping.getCreatorUid());
		getReadWriteDao().insert(siyinUserPrinterMapping);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinUserPrinterMappings.class, null);
	}

	@Override
	public void updateSiyinUserPrinterMapping(SiyinUserPrinterMapping siyinUserPrinterMapping) {
		assert (siyinUserPrinterMapping.getId() != null);
		siyinUserPrinterMapping.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinUserPrinterMapping.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(siyinUserPrinterMapping);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinUserPrinterMappings.class, siyinUserPrinterMapping.getId());
	}

	@Override
	public SiyinUserPrinterMapping findSiyinUserPrinterMappingById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinUserPrinterMapping.class);
	}
	
	@Override
	public List<SiyinUserPrinterMapping> listSiyinUserPrinterMapping() {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS)
				.orderBy(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinUserPrinterMapping.class));
	}
	
	private EhSiyinUserPrinterMappingsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinUserPrinterMappingsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinUserPrinterMappingsDao getDao(DSLContext context) {
		return new EhSiyinUserPrinterMappingsDao(context.configuration());
	}

	private DSLContext getReadWriteContext() {
		return getContext(AccessSpec.readWrite());
	}

	private DSLContext getReadOnlyContext() {
		return getContext(AccessSpec.readOnly());
	}

	private DSLContext getContext(AccessSpec accessSpec) {
		return dbProvider.getDslContext(accessSpec);
	}

	@Override
	public List<SiyinUserPrinterMapping> listSiyinUserPrinterMappingByUserId(Long userId,Integer namespaceId) {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS)
				.where(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS.USER_ID.eq(userId))
				.and(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS.NAMESPACE_ID.eq(namespaceId))
				.fetch().map(r -> ConvertHelper.convert(r, SiyinUserPrinterMapping.class));
	}

	@Override
	public SiyinUserPrinterMapping findSiyinUserPrinterMappingByUserAndPrinter(Long userId, String readerName) {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS)
				.where(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS.USER_ID.eq(userId))
				.and(Tables.EH_SIYIN_USER_PRINTER_MAPPINGS.READER_NAME.eq(readerName))
				.fetchAny().map(r -> ConvertHelper.convert(r, SiyinUserPrinterMapping.class));
	}
}
