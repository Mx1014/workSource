// @formatter:off
package com.everhomes.techpark.punch;

import java.sql.Timestamp;
import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhPunchMonthReportsDao;
import com.everhomes.server.schema.tables.pojos.EhPunchMonthReports;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Component
public class PunchMonthReportProviderImpl implements PunchMonthReportProvider {

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public void createPunchMonthReport(PunchMonthReport punchMonthReport) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhPunchMonthReports.class));
		punchMonthReport.setId(id);
//		punchMonthReport.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		punchMonthReport.setCreatorUid(UserContext.current().getUser().getId());
		getReadWriteDao().insert(punchMonthReport);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhPunchMonthReports.class, null);
	}

	@Override
	public void updatePunchMonthReport(PunchMonthReport punchMonthReport) {
		assert (punchMonthReport.getId() != null); 
		getReadWriteDao().update(punchMonthReport);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhPunchMonthReports.class, punchMonthReport.getId());
	}

	@Override
	public PunchMonthReport findPunchMonthReportById(Long id) {
		assert (id != null);
		return ConvertHelper.convert(getReadOnlyDao().findById(id), PunchMonthReport.class);
	}
	
	@Override
	public List<PunchMonthReport> listPunchMonthReport() {
		return getReadOnlyContext().select().from(Tables.EH_PUNCH_MONTH_REPORTS)
				.orderBy(Tables.EH_PUNCH_MONTH_REPORTS.ID.asc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchMonthReport.class));
	}

	@Override
	public List<PunchMonthReport> listPunchMonthReport(String ownerType, Long ownerId, Integer pageSize, CrossShardListingLocator locator) {
		SelectConditionStep<Record> step = getReadOnlyContext().select().from(Tables.EH_PUNCH_MONTH_REPORTS)
				.where(Tables.EH_PUNCH_MONTH_REPORTS.OWNER_ID.eq(ownerId));
		if (null != locator && locator.getAnchor() != null) {
			step = step.and(Tables.EH_PUNCH_MONTH_REPORTS.ID.lt(locator.getAnchor()));
		}
		if (null != pageSize) {
			step.limit(pageSize);
		}
		return step.orderBy(Tables.EH_PUNCH_MONTH_REPORTS.PUNCH_MONTH.desc())
				.fetch().map(r -> ConvertHelper.convert(r, PunchMonthReport.class));
	}

	@Override
	public PunchMonthReport findPunchMonthReportByOwnerMonth(Long ownerId, String punchMonth) {

		Record record = getReadOnlyContext().select().from(Tables.EH_PUNCH_MONTH_REPORTS)
				.where(Tables.EH_PUNCH_MONTH_REPORTS.OWNER_ID.eq(ownerId))
				.and(Tables.EH_PUNCH_MONTH_REPORTS.PUNCH_MONTH.eq(punchMonth))
				.orderBy(Tables.EH_PUNCH_MONTH_REPORTS.ID.asc())
				.fetchAny();
		if(null == record)
			return null;
		return record.map(r -> ConvertHelper.convert(r, PunchMonthReport.class));
	}

	private EhPunchMonthReportsDao getReadWriteDao() {
		return getDao(getReadWriteContext());
	}

	private EhPunchMonthReportsDao getReadOnlyDao() {
		return getDao(getReadOnlyContext());
	}

	private EhPunchMonthReportsDao getDao(DSLContext context) {
		return new EhPunchMonthReportsDao(context.configuration());
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
}
