package com.everhomes.service_agreement;

import java.util.ArrayList;
import java.util.List;

import com.everhomes.rest.service_agreement.admin.ProtocolTemplateStatus;
import com.everhomes.server.schema.tables.daos.EhProtocolTemplateVariablesDao;
import com.everhomes.server.schema.tables.daos.EhProtocolTemplatesDao;
import com.everhomes.server.schema.tables.daos.EhProtocolVariablesDao;
import com.everhomes.server.schema.tables.daos.EhProtocolsDao;
import com.everhomes.server.schema.tables.pojos.EhProtocolTemplateVariables;
import com.everhomes.server.schema.tables.pojos.EhProtocolTemplates;
import com.everhomes.server.schema.tables.pojos.EhProtocolVariables;
import com.everhomes.server.schema.tables.pojos.EhProtocols;
import org.elasticsearch.common.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.namespace.Namespace;
import com.everhomes.namespace.NamespaceProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.service_agreement.ServiceAgreementDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhServiceAgreementDao;
import com.everhomes.server.schema.tables.pojos.EhServiceAgreement;
import com.everhomes.util.ConvertHelper;

/**
 * 服务协议 ProviderImpl
 * @author huanglm
 *
 */
@Component
public class ServiceAgreementProviderImpl implements ServiceAgreementProvider {

	//private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAgreementProviderImpl.class);

	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private SequenceProvider sequenceProvider;
	
	@Autowired
    private NamespaceProvider namespaceProvider;
	
	 @Autowired
	 private OrganizationProvider organizationProvider;
	
	@Override
	public ServiceAgreementDTO getByNamespaceIdAndDef2Null(Integer namespaceId) {
		ServiceAgreementDTO resultdto = getServiceAgreementByNamespaceId(namespaceId);	
		if(resultdto != null){
			return resultdto;
		}else{
			//如果用户没有配置，我们将返回默认的协议（默认协议放在0域空间）
			String agreementContent = getDefaultServiceAgreement(namespaceId) ;
			ServiceAgreementDTO dto = new ServiceAgreementDTO(); 
			dto.setAgreementContent(agreementContent);
			return dto ;
		}

	}
	
	@Override
	public ServiceAgreementDTO getServiceAgreementByNamespaceId(Integer namespaceId) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = null ;
		//默认域空间ID是不能为空的，若可为空该逻辑得改		
		condition = Tables.EH_SERVICE_AGREEMENT.NAMESPACE_ID.eq(namespaceId);				
		//创建返回对象
		List<ServiceAgreement> result  = new ArrayList<ServiceAgreement>();
		
		SelectJoinStep<Record> query = context.select().from(Tables.EH_SERVICE_AGREEMENT);
		query.where(condition);
		//默认ID倒序（更容易看到新增的）
		query.orderBy(Tables.EH_SERVICE_AGREEMENT.ID.desc());						
		query.fetch().map((r) -> {	
			ServiceAgreement bo = ConvertHelper.convert(r, ServiceAgreement.class);			
			result.add(bo);
			return null;
		});		
		if(result != null && result.size()>0){
			return ConvertHelper.convert(result.get(0), ServiceAgreementDTO.class);
		}
		return null ;

	}

	@Override
	public void crteateServiceAgreement(ServiceAgreement bo) {
		//获取主键序列
		Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAgreement.class));
		
		bo.setId(id.intValue());
						
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAgreementDao dao = new EhServiceAgreementDao(context.configuration());
		dao.insert(bo);
				
		//广播插入数据事件
		DaoHelper.publishDaoAction(DaoAction.CREATE, EhServiceAgreement.class, null);

	}

	@Override
	public void updateServiceAgreement(ServiceAgreement bo) {
		
		DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
		EhServiceAgreementDao dao = new EhServiceAgreementDao(context.configuration());
		dao.update(bo);
		
		//广播更新数据事件
		DaoHelper.publishDaoAction(DaoAction.MODIFY, EhServiceAgreement.class, bo.getId().longValue());

	}

	@Override
	public ProtocolTemplates getActiveProtocolTemplate(Byte type) {
		DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
		Condition condition = Tables.EH_PROTOCOL_TEMPLATES.STATUS.eq(ProtocolTemplateStatus.RUNNING.getCode())
                .and(Tables.EH_PROTOCOL_TEMPLATES.TYPE.eq(type));
		SelectJoinStep<Record> query = context.select().from(Tables.EH_PROTOCOL_TEMPLATES);
		query.where(condition);

		return ConvertHelper.convert(query.fetchOne(), ProtocolTemplates.class);
	}

    @Override
    public ProtocolTemplates getProtocolTemplateById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolTemplatesDao dao = new EhProtocolTemplatesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), ProtocolTemplates.class);
    }

    @Override
    public void updateProtocolTemplate(ProtocolTemplates protocolTemplates) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolTemplatesDao dao = new EhProtocolTemplatesDao(context.configuration());
        dao.update(protocolTemplates);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhProtocolTemplates.class, protocolTemplates.getId());
    }

    @Override
    public Long createProtocolTemplate(ProtocolTemplates protocolTemplates) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProtocolTemplates.class));
        protocolTemplates.setId(id);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolTemplatesDao dao = new EhProtocolTemplatesDao(context.configuration());
        dao.insert(protocolTemplates);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProtocolTemplates.class, null);
        return id;
    }

    @Override
    public void createProtocolTemplateVariable(ProtocolTemplateVariables protocolTemplateVariables) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProtocolTemplateVariables.class));
        protocolTemplateVariables.setId(id);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolTemplateVariablesDao dao = new EhProtocolTemplateVariablesDao(context.configuration());
        dao.insert(protocolTemplateVariables);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProtocolTemplateVariables.class, null);
    }

    @Override
    public List<ProtocolTemplateVariables> getProtocolTemplateVariables(Long ownerId) {
	    List<ProtocolTemplateVariables> variables = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_PROTOCOL_TEMPLATE_VARIABLES.OWNER_ID.eq(ownerId);
        SelectJoinStep<Record> query = context.select().from(Tables.EH_PROTOCOL_TEMPLATE_VARIABLES);
        query.where(condition);
        query.fetch().map(r ->{
            variables.add(ConvertHelper.convert(r, ProtocolTemplateVariables.class));
            return null;
        });
        return variables;
    }

    @Override
    public List<ProtocolVariables> getProtocolVariables(Long ownerId, Integer namespaceId) {
        List<ProtocolVariables> variables = new ArrayList<>();
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_PROTOCOL_VARIABLES.OWNER_ID.eq(ownerId)
                .and(Tables.EH_PROTOCOL_VARIABLES.NAMESPACE_ID.eq(namespaceId));
        SelectJoinStep<Record> query = context.select().from(Tables.EH_PROTOCOL_VARIABLES);
        query.where(condition);
        query.fetch().map(r ->{
            variables.add(ConvertHelper.convert(r, ProtocolVariables.class));
            return null;
        });
        return variables;
	}

    @Override
    public Protocols getProtocols(Integer namespaceId, Byte type) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        Condition condition = Tables.EH_PROTOCOLS.STATUS.eq(ProtocolTemplateStatus.RUNNING.getCode())
                .and(Tables.EH_PROTOCOLS.NAMESPACE_ID.eq(namespaceId)).and(Tables.EH_PROTOCOLS.TYPE.eq(type));
        SelectJoinStep<Record> query = context.select().from(Tables.EH_PROTOCOLS);
        query.where(condition);

        return ConvertHelper.convert(query.fetchOne(), Protocols.class);
	}

    @Override
    public Protocols getProtocolsById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolsDao dao = new EhProtocolsDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Protocols.class);
    }

    @Override
    public void updateProtocol(Protocols protocols) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolsDao dao = new EhProtocolsDao(context.configuration());
        dao.update(protocols);

        DaoHelper.publishDaoAction(DaoAction.MODIFY, EhProtocolTemplates.class, protocols.getId());
    }

    @Override
    public Long createProtocol(Protocols protocols) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProtocols.class));
        protocols.setId(id);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolsDao dao = new EhProtocolsDao(context.configuration());
        dao.insert(protocols);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProtocols.class, null);
        return id;
    }

    @Override
    public void createProtocolVariables(ProtocolVariables protocolVariables) {
        Long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhProtocolVariables.class));
        protocolVariables.setId(id);

        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhProtocolVariablesDao dao = new EhProtocolVariablesDao(context.configuration());
        dao.insert(protocolVariables);

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhProtocolVariables.class, null);
    }

    /**
	 * 取默认协议模板
	 * @return
	 */
	private String getDefaultServiceAgreement(Integer namespaceId){
		
		ServiceAgreementDTO resultdto = getServiceAgreementByNamespaceId(Namespace.DEFAULT_NAMESPACE);	
		if(resultdto != null){
			//查域空间名
			Namespace namespace = namespaceProvider.findNamespaceById(namespaceId);
			String namespaceName = "Not Found";
			String management = "Not Found";
			String managementFull = "Not Found";
            if(namespace != null) {
            	namespaceName = namespace.getName();    
            }
            //查询管理公司信息
            List<Organization> organizations = 
            		organizationProvider.listOrganizations(OrganizationType.PM.getCode(), namespaceId, 0L, null, null);
            
            if(organizations != null && organizations.size() >0){
            	managementFull = organizations.get(0).getName();
            	management = organizations.get(0).getName();
            	OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(organizations.get(0).getId());
            	//简称
            	if(detail != null && StringUtils.isNotBlank(detail.getDisplayName())){
            		management = detail.getDisplayName() ;
            	}
            	
            }			
			String templateText = resultdto.getAgreementContent();

			//替换模板中的变量 
	        templateText = templateText.replace("${namespaceName}", namespaceName);
	        templateText = templateText.replace("${management}", management);
	        templateText = templateText.replace("${managementFull}", managementFull);
	       
	        return templateText ;
		}else{
			//如果用户没有配置
			return "there is no Default serviceAgreement template .";
		}
	}
	
	
}
