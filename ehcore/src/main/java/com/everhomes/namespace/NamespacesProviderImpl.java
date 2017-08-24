package com.everhomes.namespace;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.namespace.MaskDTO;
import org.hibernate.sql.Select;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.namespace.NamespaceResourceType;
import com.everhomes.rest.namespace.admin.NamespaceInfoDTO;
import com.everhomes.schema.tables.daos.EhNamespacesDao;
import com.everhomes.schema.tables.pojos.EhNamespaces;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhNamespaceDetailsDao;
import com.everhomes.server.schema.tables.pojos.EhNamespaceDetails;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RecordHelper;

@Component
public class NamespacesProviderImpl implements NamespacesProvider {

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Override
	public void createNamespace(Namespace namespace){
		Integer id = Long.valueOf(sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNamespaces.class))).intValue();
		namespace.setId(id);
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNamespacesDao dao = new EhNamespacesDao(context.configuration());
		dao.insert(namespace);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNamespaces.class, id.longValue());
	}

	@Override
	public void createNamespaceDetail(NamespaceDetail namespaceDetail) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNamespaceDetails.class));
		namespaceDetail.setId(id);
		namespaceDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNamespaceDetailsDao dao = new EhNamespaceDetailsDao(context.configuration());
		dao.insert(namespaceDetail);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNamespaceDetails.class, id);
	}
	
	@Override
	public NamespaceInfoDTO findNamespaceByid(Integer id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.schema.tables.EhNamespaces t1 = com.everhomes.schema.Tables.EH_NAMESPACES.as("t1");
		com.everhomes.server.schema.tables.EhNamespaceDetails t2 = Tables.EH_NAMESPACE_DETAILS.as("t2");
		Record record = context.select(t1.ID, t1.NAME, t2.RESOURCE_TYPE)
							.from(t1)
							.leftOuterJoin(t2)
							.on(t1.ID.eq(t2.NAMESPACE_ID))
							.where(t1.ID.eq(id))
							.orderBy(t1.ID)
							.fetchOne();
		if(record != null){
			return RecordHelper.convert(record, NamespaceInfoDTO.class);
		}
		return null;
	}

	
	@Override
	public List<NamespaceInfoDTO> listNamespace() {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		com.everhomes.schema.tables.EhNamespaces t1 = com.everhomes.schema.Tables.EH_NAMESPACES.as("t1");
		com.everhomes.server.schema.tables.EhNamespaceDetails t2 = Tables.EH_NAMESPACE_DETAILS.as("t2");
		Result<?> result = context.select(t1.ID, t1.NAME, t2.RESOURCE_TYPE)
										.from(t1)
										.leftOuterJoin(t2)
										.on(t1.ID.eq(t2.NAMESPACE_ID))
										.orderBy(t1.ID)
										.fetch();
		if(result != null && result.isNotEmpty()){
			return result.map(r->RecordHelper.convert(r, NamespaceInfoDTO.class));
		}
		return new ArrayList<NamespaceInfoDTO>();
	}

	@Override
	public void updateNamespace(Namespace namespace) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNamespacesDao dao = new EhNamespacesDao(context.configuration());
		dao.update(namespace);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNamespaces.class, namespace.getId().longValue());
	}

	@Override
	public void updateNamespaceDetail(NamespaceDetail namespaceDetail) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNamespaceDetailsDao dao = new EhNamespaceDetailsDao(context.configuration());
		dao.update(namespaceDetail);
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhNamespaces.class, namespaceDetail.getId());
	}

	@Override
	public NamespaceDetail findNamespaceDetailByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		com.everhomes.server.schema.tables.EhNamespaceDetails t1 = Tables.EH_NAMESPACE_DETAILS.as("t1");
		Record record = context.select().from(t1).where(t1.NAMESPACE_ID.eq(namespaceId)).fetchOne();
		if (record != null) {
			return ConvertHelper.convert(record, NamespaceDetail.class);
		}
		return null;
	}

	@Override
	public List<NamespaceResource> listNamespaceResources(Integer namespaceId, String resourceType) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		return context.select().from(Tables.EH_NAMESPACE_RESOURCES)
		.where(Tables.EH_NAMESPACE_RESOURCES.NAMESPACE_ID.eq(namespaceId))
		.and(Tables.EH_NAMESPACE_RESOURCES.RESOURCE_TYPE.eq(resourceType))
		.fetch()
		.map(r->ConvertHelper.convert(r, NamespaceResource.class));
	}

	@Override
	public List<MaskDTO> listNamespaceMasks(Integer namespaceId) {
		return null;
	}


}
