package com.everhomes.techpark.expansion;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.pmtask.PmTask;
import com.everhomes.rest.techpark.expansion.ApplyEntryStatus;
import com.everhomes.rest.techpark.expansion.LeaseIssuerStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhLeaseIssuerAddressesDao;
import com.everhomes.server.schema.tables.daos.EhLeaseIssuersDao;
import com.everhomes.server.schema.tables.pojos.EhLeaseConfigs;
import com.everhomes.server.schema.tables.pojos.EhLeaseConfigs2;
import com.everhomes.server.schema.tables.pojos.EhLeaseIssuerAddresses;
import com.everhomes.server.schema.tables.pojos.EhLeaseIssuers;
import com.everhomes.server.schema.tables.records.EhLeaseConfigs2Record;
import com.everhomes.server.schema.tables.records.EhLeaseConfigsRecord;
import com.everhomes.server.schema.tables.records.EhLeaseIssuerAddressesRecord;
import com.everhomes.server.schema.tables.records.EhLeaseIssuersRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.*;
import org.jooq.impl.DefaultRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class EnterpriseLeaseIssuerProviderImpl implements EnterpriseLeaseIssuerProvider {
	
	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private SequenceProvider sequenceProvider;

	@Override
	public LeaseIssuer getLeaseIssuerById(Long id) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseIssuers.class));
		
		SelectQuery<EhLeaseIssuersRecord> query = context.selectQuery(Tables.EH_LEASE_ISSUERS);
		query.addConditions(Tables.EH_LEASE_ISSUERS.ID.eq(id));
		
		return ConvertHelper.convert(query.fetchOne(), LeaseIssuer.class);
	}

	@Override
	public void createLeaseIssuer(LeaseIssuer leaseIssuer) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhLeaseIssuers.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseIssuers.class));
		leaseIssuer.setId(id);
		leaseIssuer.setCreatorUid(UserContext.current().getUser().getId());
		leaseIssuer.setCreateTime(new Timestamp(System.currentTimeMillis()));
		leaseIssuer.setStatus(LeaseIssuerStatus.ACTIVE.getCode());

		EhLeaseIssuersDao dao = new EhLeaseIssuersDao(context.configuration());
		dao.insert(leaseIssuer);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeaseIssuers.class, null);

	}

	@Override
	public void updateLeaseIssuer(LeaseIssuer leaseIssuer) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseIssuers.class));
		EhLeaseIssuersDao dao = new EhLeaseIssuersDao(context.configuration());
		dao.update(leaseIssuer);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLeaseIssuers.class, null);

	}

	@Override
	public void deleteLeaseIssuer(LeaseIssuer leaseIssuer) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseIssuers.class));
		EhLeaseIssuersDao dao = new EhLeaseIssuersDao(context.configuration());
		dao.delete(leaseIssuer);

		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLeaseIssuers.class, null);

	}

	@Override
	public List<LeaseIssuer> listLeaseIssers(Integer namespaceId, Long organizationId, String keyword, Long pageAnchor, Integer pageSize) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseIssuers.class));
		SelectQuery<EhLeaseIssuersRecord> query = context.selectQuery(Tables.EH_LEASE_ISSUERS);
		query.addConditions(Tables.EH_LEASE_ISSUERS.STATUS.eq(LeaseIssuerStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_LEASE_ISSUERS.NAMESPACE_ID.eq(namespaceId));
		if (StringUtils.isNotBlank(keyword)) {
			keyword = "%" + keyword + "%";
			query.addJoin(Tables.EH_ORGANIZATIONS, JoinType.LEFT_OUTER_JOIN, Tables.EH_ORGANIZATIONS.ID.eq(Tables.EH_LEASE_ISSUERS.ENTERPRISE_ID));

			query.addConditions(Tables.EH_LEASE_ISSUERS.ISSUER_CONTACT.like(keyword).or(
					Tables.EH_LEASE_ISSUERS.ISSUER_NAME.like(keyword))
					.or(Tables.EH_ORGANIZATIONS.NAME.like(keyword)));
		}

		if (null != organizationId)
			query.addConditions(Tables.EH_LEASE_ISSUERS.ENTERPRISE_ID.eq(organizationId));

		if (null != pageAnchor)
			query.addConditions(Tables.EH_LEASE_ISSUERS.ID.le(pageAnchor));

		query.addOrderBy(Tables.EH_LEASE_ISSUERS.ID.desc());

		if (null != pageSize)
			query.addLimit(pageSize);


		return 	query.fetch().map(new DefaultRecordMapper(Tables.EH_LEASE_ISSUERS.recordType(), LeaseIssuer.class));

	}

	@Override
	public LeaseIssuer fingLeaseIssersByOrganizationId(Integer namespaceId, Long organizationId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseIssuers.class));
		SelectQuery<EhLeaseIssuersRecord> query = context.selectQuery(Tables.EH_LEASE_ISSUERS);
		query.addConditions(Tables.EH_LEASE_ISSUERS.STATUS.eq(LeaseIssuerStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_LEASE_ISSUERS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_LEASE_ISSUERS.ENTERPRISE_ID.eq(organizationId));

		return 	ConvertHelper.convert(query.fetchAny(), LeaseIssuer.class);

	}

	@Override
	public LeaseIssuer findLeaseIssersByContact(Integer namespaceId, String contact) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseIssuers.class));
		SelectQuery<EhLeaseIssuersRecord> query = context.selectQuery(Tables.EH_LEASE_ISSUERS);
		query.addConditions(Tables.EH_LEASE_ISSUERS.STATUS.eq(LeaseIssuerStatus.ACTIVE.getCode()));
		query.addConditions(Tables.EH_LEASE_ISSUERS.NAMESPACE_ID.eq(namespaceId));
		query.addConditions(Tables.EH_LEASE_ISSUERS.ISSUER_CONTACT.eq(contact));

		return 	ConvertHelper.convert(query.fetchAny(), LeaseIssuer.class);

	}

	@Override
	public LeasePromotionConfig getLeasePromotionConfigByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseConfigs.class));

		SelectQuery<EhLeaseConfigsRecord> query = context.selectQuery(Tables.EH_LEASE_CONFIGS);
		query.addConditions(Tables.EH_LEASE_CONFIGS.NAMESPACE_ID.eq(namespaceId));

		return ConvertHelper.convert(query.fetchOne(), LeasePromotionConfig.class);
	}

	@Override
	public List<LeasePromotionConfig2> listLeasePromotionConfigByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseConfigs2.class));

		SelectQuery<EhLeaseConfigs2Record> query = context.selectQuery(Tables.EH_LEASE_CONFIGS2);
		query.addConditions(Tables.EH_LEASE_CONFIGS2.NAMESPACE_ID.eq(namespaceId));

		return query.fetch().map(r -> ConvertHelper.convert(r, LeasePromotionConfig2.class));
	}

	@Override
	public void createLeaseIssuerAddress(LeaseIssuerAddress leaseIssuerAddress) {
		long id = sequenceProvider.getNextSequence(NameMapper
				.getSequenceDomainFromTablePojo(EhLeaseIssuers.class));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseIssuerAddresses.class));
		leaseIssuerAddress.setId(id);
		leaseIssuerAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
		EhLeaseIssuerAddressesDao dao = new EhLeaseIssuerAddressesDao(context.configuration());
		dao.insert(leaseIssuerAddress);

		DaoHelper.publishDaoAction(DaoAction.CREATE, EhLeaseIssuerAddresses.class, null);

	}

	@Override
	public void deleteLeaseIssuerAddressByLeaseIssuerId(Long leaseIssuerId) {

		DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhLeaseIssuerAddresses.class));

		DeleteQuery query = context.deleteQuery(Tables.EH_LEASE_ISSUER_ADDRESSES);
		query.addConditions(Tables.EH_LEASE_ISSUER_ADDRESSES.LEASE_ISSUER_ID.eq(leaseIssuerId));

		query.execute();
	}

	@Override
	public List<LeaseIssuerAddress> listLeaseIsserAddresses(Long leaseIssuerId, Long buildingId) {
		List<LeaseIssuerAddress> result = new ArrayList<>();

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseIssuers.class));
		SelectQuery<EhLeaseIssuerAddressesRecord> query = context.selectQuery(Tables.EH_LEASE_ISSUER_ADDRESSES);
		query.addConditions(Tables.EH_LEASE_ISSUER_ADDRESSES.LEASE_ISSUER_ID.eq(leaseIssuerId));

		if (null != buildingId) {
			query.addConditions(Tables.EH_LEASE_ISSUER_ADDRESSES.BUILDING_ID.eq(buildingId));
		}
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, LeaseIssuerAddress.class));
			return null;
		});

		return result;
	}

	@Override
	public List<LeaseIssuerAddress> listLeaseIsserBuildings(Long leaseIssuerId) {
		List<LeaseIssuerAddress> result = new ArrayList<>();

		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhLeaseIssuers.class));
		SelectQuery<EhLeaseIssuerAddressesRecord> query = context.selectQuery(Tables.EH_LEASE_ISSUER_ADDRESSES);
		query.addConditions(Tables.EH_LEASE_ISSUER_ADDRESSES.LEASE_ISSUER_ID.eq(leaseIssuerId));

		query.addGroupBy(Tables.EH_LEASE_ISSUER_ADDRESSES.BUILDING_ID);
		query.fetch().map((r) -> {
			result.add(ConvertHelper.convert(r, LeaseIssuerAddress.class));
			return null;
		});

		return result;
	}
}
