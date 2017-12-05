// @formatter:off
package com.everhomes.me_menu;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.module.*;
import com.everhomes.naming.NameMapper;
import com.everhomes.rest.module.ServiceModuleStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhMeWebMenusDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentRelationsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModuleAssignmentsDao;
import com.everhomes.server.schema.tables.daos.EhServiceModulesDao;
import com.everhomes.server.schema.tables.pojos.*;
import com.everhomes.server.schema.tables.records.*;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MeWebMenuProviderImpl implements MeWebMenuProvider {

    @Autowired
    private DbProvider dbProvider;

    @Override
    public List<MeWebMenu> listMeWebMenus(Integer namespaceId){
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        SelectQuery<EhMeWebMenusRecord> query = context.selectQuery(Tables.EH_ME_WEB_MENUS);
        query.addConditions(Tables.EH_ME_WEB_MENUS.NAMESPACE_ID.eq(namespaceId));
        query.addOrderBy(Tables.EH_ME_WEB_MENUS.POSITION_FLAG.asc());
        query.addOrderBy(Tables.EH_ME_WEB_MENUS.SORT_NUM.asc());
        List<MeWebMenu> list = query.fetch().map(r ->
                ConvertHelper.convert(r, MeWebMenu.class)
        );
        return list;
    }

    @Override
    public MeWebMenu findMeWebMenuById(Long id) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        EhMeWebMenusDao dao = new EhMeWebMenusDao(context.configuration());
        EhMeWebMenus result = dao.findById(id);
        if (result == null) {
            return null;
        }
        return ConvertHelper.convert(result, MeWebMenu.class);
    }


}
