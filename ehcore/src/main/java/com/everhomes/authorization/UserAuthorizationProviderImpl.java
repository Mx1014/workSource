// @formatter:off
package com.everhomes.authorization;

public class UserAuthorizationProviderImpl {

//
//	private static final Logger LOGGER = LoggerFactory.getLogger(SiyinPrintRecordProviderImpl.class);
//
//	@Autowired
//	private DbProvider dbProvider;
//
//	@Autowired
//	private SequenceProvider sequenceProvider;
//
//	@Override
//	public void createSiyinPrintRecord(SiyinPrintRecord siyinPrintRecord) {
//		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhSiyinPrintRecords.class));
//		siyinPrintRecord.setId(id);
//		siyinPrintRecord.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		siyinPrintRecord.setOperateTime(siyinPrintRecord.getCreateTime());
//		getReadWriteDao().insert(siyinPrintRecord);
//		DaoHelper.publishDaoAction(DaoAction.CREATE, EhSiyinPrintRecords.class, null);
//	}
//
//	@Override
//	public void updateSiyinPrintRecord(SiyinPrintRecord siyinPrintRecord) {
//		assert (siyinPrintRecord.getId() != null);
////		siyinPrintRecord.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
//		siyinPrintRecord.setOperatorUid(UserContext.current().getUser().getId());
//		getReadWriteDao().update(siyinPrintRecord);
//		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhSiyinPrintRecords.class, siyinPrintRecord.getId());
//	}
//
//	@Override
//	public SiyinPrintRecord findSiyinPrintRecordById(Long id) {
//		assert (id != null);
//		return ConvertHelper.convert(getReadOnlyDao().findById(id), SiyinPrintRecord.class);
//	}
//	
//	@Override
//	public List<SiyinPrintRecord> listSiyinPrintRecord() {
//		return getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_RECORDS)
//				.orderBy(Tables.EH_SIYIN_PRINT_RECORDS.ID.asc())
//				.fetch().map(r -> ConvertHelper.convert(r, SiyinPrintRecord.class));
//	}
//	
//	private EhSiyinPrintRecordsDao getReadWriteDao() {
//		return getDao(getReadWriteContext());
//	}
//
//	private EhSiyinPrintRecordsDao getReadOnlyDao() {
//		return getDao(getReadOnlyContext());
//	}
//
//	private EhSiyinPrintRecordsDao getDao(DSLContext context) {
//		return new EhSiyinPrintRecordsDao(context.configuration());
//	}
//
//	private DSLContext getReadWriteContext() {
//		return getContext(AccessSpec.readWrite());
//	}
//
//	private DSLContext getReadOnlyContext() {
//		return getContext(AccessSpec.readOnly());
//	}
//
//	private DSLContext getContext(AccessSpec accessSpec) {
//		return dbProvider.getDslContext(accessSpec);
//	}
//
//	@Override
//	public SiyinPrintRecord findSiyinPrintRecordByJobId(String jobId) {
//		SelectConditionStep<?> query = getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_RECORDS)
//			.where(Tables.EH_SIYIN_PRINT_RECORDS.JOB_ID.eq(jobId));
//		LOGGER.info("findSiyinPrintRecordByJobId sql = {},param = {}",query.getSQL(),query.getBindValues());
//		List<SiyinPrintRecord> list  = query.fetch().map(r->ConvertHelper.convert(r, SiyinPrintRecord.class));
//		if(list!=null && list.size() > 0)
//			return list.get(0);
//		return null;
//	}
//
//	@Override
//	public List<SiyinPrintRecord> listSiyinPrintRecordByOrderId(Long creatorUid, Long orderId,String ownerType, Long ownerId) {
//		SelectConditionStep<?> query =  getReadOnlyContext().select().from(Tables.EH_SIYIN_PRINT_RECORDS)
//				.where(Tables.EH_SIYIN_PRINT_RECORDS.CREATOR_UID.eq(creatorUid))
//				.and(Tables.EH_SIYIN_PRINT_RECORDS.ORDER_ID.eq(orderId))
//				.and(Tables.EH_SIYIN_PRINT_RECORDS.OWNER_TYPE.eq(ownerType))
//				.and(Tables.EH_SIYIN_PRINT_RECORDS.OWNER_ID.eq(ownerId))
//				.and(Tables.EH_SIYIN_PRINT_RECORDS.STATUS.eq(CommonStatus.ACTIVE.getCode()));
//		LOGGER.info("listSiyinPrintRecordByOrderId sql = {},param = {}",query.getSQL(),query.getBindValues());
//		List<SiyinPrintRecord> list = query.fetch().map(r->ConvertHelper.convert(r, SiyinPrintRecord.class));
//		return list;
//	}



}
