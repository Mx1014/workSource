package com.everhomes.point;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.ListingLocator;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserScoresDao;
import com.everhomes.server.schema.tables.pojos.EhUserScores;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhOrganizationsRecord;
import com.everhomes.server.schema.tables.records.EhUserScoresRecord;
import com.everhomes.server.schema.tables.records.EhUsersRecord;
import com.everhomes.user.User;
import com.everhomes.util.ConvertHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPointProviderImpl implements UserPointProvider {
    @Autowired
    private DbProvider dbProvider;

    @Override
    public void addUserPoint(UserScore score) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserScores.class));
        EhUserScoresDao dao = new EhUserScoresDao(context.configuration());
        dao.insert(score);
    }

    @Override
    public UserScore findTodayRecord(Operator op, Condition... conditions) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserScores.class));
        SelectQuery<EhUserScoresRecord> query = context.selectQuery(Tables.EH_USER_SCORES);
        query.addConditions(op, conditions);
        List<UserScore> ret = query.fetch().stream().map(r -> ConvertHelper.convert(r, UserScore.class))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ret)) {
            return null;
        }
        return ret.get(0);
    }

    @Override
    public List<UserScore> listUserScore(ListingLocator locator, int count, Operator op, Condition... conditions) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWriteWith(EhUserScores.class));
        SelectQuery<EhUserScoresRecord> query = context.selectQuery(Tables.EH_USER_SCORES);
        query.addConditions(op, conditions);
        if (locator.getAnchor() != null)
            query.addConditions(Tables.EH_USER_SCORES.ID.gt(locator.getAnchor()));
        query.addLimit(count);
        List<UserScore> userScores = query.fetch().stream().map(r -> ConvertHelper.convert(r, UserScore.class))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userScores) || userScores.size() < count) {
            locator.setAnchor(null);
        } else {
            locator.setAnchor(userScores.get(userScores.size() - 1).getId());
        }
        return userScores.subList(0, userScores.size() - 1);
    }



}
