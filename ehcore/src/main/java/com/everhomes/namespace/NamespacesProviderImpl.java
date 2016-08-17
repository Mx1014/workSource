package com.everhomes.namespace;

import java.sql.Timestamp;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.schema.tables.daos.EhNamespacesDao;
import com.everhomes.schema.tables.pojos.EhNamespaces;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.daos.EhNamespaceDetailsDao;
import com.everhomes.server.schema.tables.pojos.EhNamespaceDetails;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

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
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNamespaces.class, null);
	}

	@Override
	public void createNamespaceDetail(NamespaceDetail namespaceDetail) {
		Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhNamespaceDetails.class));
		namespaceDetail.setId(id);
		namespaceDetail.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhNamespaceDetailsDao dao = new EhNamespaceDetailsDao(context.configuration());
		dao.insert(namespaceDetail);
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhNamespaceDetails.class, null);
	}
	
	@Override
	public Namespace findNamespaceByid(Integer id){
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		EhNamespacesDao dao = new EhNamespacesDao(context.configuration());
		return ConvertHelper.convert(dao.findById(id), Namespace.class);
	}
}
