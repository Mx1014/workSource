// @formatter:off
package com.everhomes.link;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhLinksDao;
import com.everhomes.server.schema.tables.pojos.EhLinks;
import com.everhomes.server.schema.tables.records.EhLinksRecord;
import com.everhomes.util.ConvertHelper;
@Component
public class LinkProviderImpl implements LinkProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkProviderImpl.class);
    
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public void createLink(Link link) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhLinksDao dao = new EhLinksDao(context.configuration()); 
        dao.insert(link); 

        DaoHelper.publishDaoAction(DaoAction.CREATE, EhLinks.class, null); 
        
    }

    @CacheEvict(value="Link", key="#link.id")
    @Override
    public void updateLink(Link link){
    	assert(link.getId() == null);
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhLinksDao dao = new EhLinksDao(context.configuration());
    	dao.update(link);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLinks.class, link.getId());
    }
    
    @CacheEvict(value="Link", key="#link.id")
    @Override
    public void deleteLink(Link link){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhLinksDao dao = new EhLinksDao(context.configuration());
    	dao.deleteById(link.getId());
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLinks.class, link.getId());
    }
    
    @CacheEvict(value="Link", key="#id")
    @Override
    public void deleteLinkById(Long id){
    	
    	DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
    	EhLinksDao dao = new EhLinksDao(context.configuration());
    	dao.deleteById(id);
    	
    	DaoHelper.publishDaoAction(DaoAction.MODIFY, EhLinks.class, id);
    }
     
    @Cacheable(value="Link", key="#id")
    @Override
    public Link findLinkById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhLinksDao dao = new EhLinksDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), Link.class);
    }
    
    @Cacheable(value="Link-post", key="#postId")
    @Override
    public Link findLinkByPostId(Long postId) {
    	final Link[] result = new Link[1];
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhLinksRecord> query = context.selectQuery(Tables.EH_LINKS);
        query.addConditions(Tables.EH_LINKS.SOURCE_ID.eq(postId));
        
        query.fetch().map((r) -> {
      	   if(r != null)
      		  result[0] = ConvertHelper.convert(r, Link.class);
      	   return null;
         });
         
         return result[0];
    }
    
   
    @Override
    public List<Link> listLinksByPostId(Long postId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());

        List<Link> result  = new ArrayList<Link>();
        SelectQuery<EhLinksRecord> query = context.selectQuery(Tables.EH_LINKS);
        query.addOrderBy(Tables.EH_LINKS.ID.desc());
        query.fetch().map((r) -> {
        	result.add(ConvertHelper.convert(r, Link.class));
            return null;
        });
        return result;
    }
}
