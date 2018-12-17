// @formatter:off
package com.everhomes.print;

import java.sql.Timestamp;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSiyinPrintPrintersDao;
import com.everhomes.server.schema.tables.pojos.EhSiyinPrintPrinters;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class SiyinPrintPrinterProviderImpl implements SiyinPrintPrinterProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintPrinterProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createSiyinPrintPrinter(SiyinPrintPrinter siyinPrintPrinter) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintPrinters.class));
		siyinPrintPrinter.setId(id);
		siyinPrintPrinter.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintPrinter.setCreatorUid(UserContext.current().getUser().getId());
//		siyinPrintPrinter.setUpdateTime(siyinPrintPrinter.getCreateTime());
		siyinPrintPrinter.setOperatorUid(siyinPrintPrinter.getCreatorUid());
		getReadWriteDao().insert(siyinPrintPrinter);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintPrinters.class, null);
	}

	@Override
	public void updateSiyinPrintPrinter(SiyinPrintPrinter siyinPrintPrinter) {
		assert (siyinPrintPrinter.getId() != null);
//		siyinPrintPrinter.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		siyinPrintPrinter.setOperatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().update(siyinPrintPrinter);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintPrinters.class, siyinPrintPrinter.getId());
	}

	@Override
	public SiyinPrintPrinter findSiyinPrintPrinterById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintPrinter.class);
	}
	
	@Override
	public List<SiyinPrintPrinter> findSiyinPrintPrinterByOwnerId(Long ownerId) {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_PRINTERS)
				.where(Tables.EH_SIYIN_PRINT_PRINTERS.OWNER_ID.eq(ownerId))
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintPrinter.class));
	}
	
	@Override
	public List<SiyinPrintPrinter> findSiyinPrintPrinterByNamespaceId(Integer namespaceId) {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_PRINTERS)
				.where(Tables.EH_SIYIN_PRINT_PRINTERS.NAMESPACE_ID.eq(namespaceId))
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintPrinter.class));
	}
	
	@Override
	public List<SiyinPrintPrinter> listSiyinPrintPrinter(Integer namespaceId, String ownerType, Long ownerId) {
		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_PRINTERS)
				.where(Tables.EH_SIYIN_PRINT_PRINTERS.NAMESPACE_ID.eq(namespaceId)
						.and(Tables.EH_SIYIN_PRINT_PRINTERS.OWNER_TYPE.eq(ownerType))
						.and(Tables.EH_SIYIN_PRINT_PRINTERS.OWNER_ID.eq(ownerId)))
				.orderBy(Tables.EH_SIYIN_PRINT_PRINTERS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintPrinter.class));
	}
	
	private EhSiyinPrintPrintersDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhSiyinPrintPrintersDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhSiyinPrintPrintersDao getDao(DSLContext context) {
		return new EhSiyinPrintPrintersDao(context.configuration());
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
	public SiyinPrintPrinter findSiyinPrintPrinterByReadName(String readerName) {
		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_PRINTERS)
				.where(Tables.EH_SIYIN_PRINT_PRINTERS.READER_NAME.eq(readerName))
				.and(Tables.EH_SIYIN_PRINT_PRINTERS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
		LOGGER.info("findSiyinPrintPrinterByReadName sql = {},param = {}",query.getSQL(),query.getBindValues());
		List<SiyinPrintPrinter> list = query.fetch().map(r->ConvertHelper.convert(r, SiyinPrintPrinter.class));
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
