package com.everhomes.visitorsys;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhVisitorSysHkwsUserDao;
import com.everhomes.server.schema.tables.pojos.EhVisitorSysHkwsUser;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.lang.StringUtils;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HKWSUserProviderImpl implements HKWSUserProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public void createUser(HKWSUser user) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysHkwsUser.class));
        EhVisitorSysHkwsUserDao dao = new EhVisitorSysHkwsUserDao(context.configuration());
        dao.insert(user);
    }

    @Override
    public void createUsers(List<HKWSUser> users) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysHkwsUser.class));
        EhVisitorSysHkwsUserDao dao = new EhVisitorSysHkwsUserDao(context.configuration());
        Collection<EhVisitorSysHkwsUser> users1 = users.stream().map(r -> ConvertHelper.convert(r,EhVisitorSysHkwsUser.class)).collect(Collectors.toList());
        dao.insert(users1);
    }

    @Override
    public void deleteUser(Integer personId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysHkwsUser.class));
        EhVisitorSysHkwsUserDao dao = new EhVisitorSysHkwsUserDao(context.configuration());
        dao.deleteById(personId);
    }

    @Override
    public void deleteUsers(List<Integer> personIds) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysHkwsUser.class));
        EhVisitorSysHkwsUserDao dao = new EhVisitorSysHkwsUserDao(context.configuration());
        dao.deleteById(personIds);
    }

    @Override
    public HKWSUser findUserById(Integer personId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhVisitorSysHkwsUser.class));
        EhVisitorSysHkwsUserDao dao = new EhVisitorSysHkwsUserDao(context.configuration());
        EhVisitorSysHkwsUser result = dao.findById(personId);
        return ConvertHelper.convert(result,HKWSUser.class);
    }

    @Override
    public List<HKWSUser> findUserByPhone(String phone) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnlyWith(EhVisitorSysHkwsUser.class));
        SelectQuery query = context.selectQuery();
        if(StringUtils.isNotBlank(phone))
            query.addConditions(Tables.EH_VISITOR_SYS_HKWS_USER.PHONE_NO.eq(phone));
        return query.fetch().map(record -> ConvertHelper.convert(record,HKWSUser.class));
    }
}
