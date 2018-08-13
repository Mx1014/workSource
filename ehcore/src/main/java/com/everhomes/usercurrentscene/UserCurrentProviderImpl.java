package com.everhomes.usercurrentscene;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.rest.usercurrentscene.GetUserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneCommand;
import com.everhomes.rest.usercurrentscene.UserCurrentSceneDTO;
import com.everhomes.sequence.SequenceProvider;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCurrentProviderImpl implements UserCurrentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCurrentProviderImpl.class);
    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public UserCurrentSceneDTO getUserCurrentSceneByUid(GetUserCurrentSceneCommand cmd) {
        return null;
    }

    @Override
    public void addUserCurrentScene(UserCurrentSceneCommand cmd) {

    }

    @Override
    public void updateUserCurrentScene(UserCurrentSceneCommand cmd) {

    }


   /* private EhUserCurrentSceneDao rwDao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        return new EhUserCurrentSceneDao(context.configuration());
    }

    private EhUserCurrentSceneDao dao() {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return new EhUserCurrentSceneDao(context.configuration());
    }
*/
    private DSLContext context() {
        return dbProvider.getDslContext(AccessSpec.readOnly());
    }

    private DSLContext rwContext() {
        return dbProvider.getDslContext(AccessSpec.readWrite());
    }
}
