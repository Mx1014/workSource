package com.everhomes.usercurrentscene;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.usercurrentscene.GetUserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneDTO;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserCurrentSceneDao;
import com.everhomes.server.schema.tables.pojos.EhUserCurrentScene;
import com.everhomes.user.UserIdentifierLog;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCurrentProviderImpl implements UserCurrentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCurrentProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public UserCurrentSceneDTO getUserCurrentSceneByUid(GetUserCurrentSceneCommand cmd) {
        UserCurrentScene bo = context().selectFrom(Tables.EH_USER_CURRENT_SCENE)
                .where(Tables.EH_USER_CURRENT_SCENE.UID.eq(cmd.getUid()))
                .and(Tables.EH_USER_CURRENT_SCENE.NAMESPACE_ID.eq(cmd.getNamespaceId()))
                .orderBy(Tables.EH_USER_CURRENT_SCENE.ID.desc())
                .fetchAnyInto(UserCurrentScene.class);
        return ConvertHelper.convert(bo, UserCurrentSceneDTO.class) ;
    }

    @Override
    public Long addUserCurrentScene(UserCurrentScene bo) {

        Long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhUserCurrentScene.class));
        bo.setId(id);
        bo.setCreateTime(DateUtils.currentTimestamp());
        rwDao().insert(bo);
        return id ;
    }

    @Override
    public void updateUserCurrentScene(UserCurrentScene bo) {

        bo.setUpdateTime(DateUtils.currentTimestamp());
        rwDao().update(bo);
    }


   private EhUserCurrentSceneDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhUserCurrentSceneDao(context.configuration());
    }

    private EhUserCurrentSceneDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhUserCurrentSceneDao(context.configuration());
    }

    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
