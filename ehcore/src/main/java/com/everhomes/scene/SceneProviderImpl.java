// @formatter:off
package com.everhomes.scene;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.scene.ListSceneTypesCommand;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhSceneTypesDao;
import com.everhomes.util.ConvertHelper;

@Component
public class SceneProviderImpl implements SceneProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(SceneProviderImpl.class);
        
    @Autowired
    private DbProvider dbProvider;
    
    @Override
    public SceneTypeInfo findSceneTypeById(long id) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        EhSceneTypesDao dao = new EhSceneTypesDao(context.configuration());
        return ConvertHelper.convert(dao.findById(id), SceneTypeInfo.class);
    }

    @Override
    public List<SceneTypeInfo> findSceneTypeByName(Integer namespaceId, String name) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        
        Condition condition = Tables.EH_SCENE_TYPES.NAME.eq(name);
        
        List<SceneTypeInfo> result  = new ArrayList<SceneTypeInfo>();
        SelectJoinStep<Record> query = context.select().from(Tables.EH_SCENE_TYPES);
        query.where(condition);
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, SceneTypeInfo.class));
            return null;
        });
        
        return result;
    }

    @Override
    public List<SceneTypeInfo> listSceneTypes(Integer namespaceId) {
        DSLContext context = this.dbProvider.getDslContext(AccessSpec.readOnly());
        
        List<SceneTypeInfo> result  = new ArrayList<SceneTypeInfo>();
        SelectJoinStep<Record> query = context.select().from(Tables.EH_SCENE_TYPES);
        if(namespaceId != null) {
            Condition condition = Tables.EH_SCENE_TYPES.NAMESPACE_ID.eq(namespaceId);
            query.where(condition);
        }
        query.fetch().map((r) -> {
            result.add(ConvertHelper.convert(r, SceneTypeInfo.class));
            return null;
        });
        
        return result;
    }
 }
