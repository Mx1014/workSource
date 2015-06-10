package com.everhomes.admin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.Condition;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.contentserver.ContentServerService;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhUserIdentifiersDao;
import com.everhomes.server.schema.tables.pojos.EhUserIdentifiers;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.server.schema.tables.records.EhUserIdentifiersRecord;
import com.everhomes.server.schema.tables.records.EhUsersRecord;
import com.everhomes.sharding.ShardIterator;
import com.everhomes.user.IdentifierType;
import com.everhomes.user.UserAdminProvider;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserInfo;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.IterationMapReduceCallback.AfterAction;

@Component
public class UserAdminProviderImpl implements UserAdminProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Autowired
    private ContentServerService contentServerService;

    @Override
    public List<UserIdentifier> listUserIdentifiers(List<Long> uids) {
        List<UserIdentifier> identifiers = new ArrayList<UserIdentifier>(uids.size());
        dbProvider.mapReduce(AccessSpec.readOnlyWith(EhUserIdentifiers.class),null,
                (context, obj) -> {
                    if (identifiers.size() >= uids.size())
                        return false;
                    EhUserIdentifiersDao dao = new EhUserIdentifiersDao(context.configuration());
                    identifiers.addAll(dao.fetchByOwnerUid(uids.toArray(new Long[uids.size()])).stream()
                            .map(r -> ConvertHelper.convert(r, UserIdentifier.class)).collect(Collectors.toList()));
                    return true;
                });
        return identifiers;
    }

    @Override
    public List<UserInfo> listRegisterByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback) {
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUsers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhUsersRecord> query = context.selectQuery(Tables.EH_USERS);
            if (callback != null)
                callback.buildCondition(locator, query);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_USERS.ID.lt(locator.getAnchor()));
            query.addOrderBy(Tables.EH_USERS.CREATE_TIME.desc());
            query.addLimit(count - userInfos.size());

            query.fetch().map((r) -> {
                userInfos.add(ConvertHelper.convert(r, UserInfo.class));
                return null;
            });

            if (userInfos.size() >= count) {
                locator.setAnchor(userInfos.get(userInfos.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (userInfos.size() > 0) {
            locator.setAnchor(userInfos.get(userInfos.size() - 1).getId());
        }

        return userInfos;
    }

    @Override
    public List<UserIdentifier> listUserIdentifiersByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback, Condition... conditons) {
        List<UserIdentifier> userIdentifiers = new ArrayList<UserIdentifier>();
        if (locator.getShardIterator() == null) {
            AccessSpec accessSpec = AccessSpec.readOnlyWith(EhUserIdentifiers.class);
            ShardIterator shardIterator = new ShardIterator(accessSpec);
            locator.setShardIterator(shardIterator);
        }
        this.dbProvider.iterationMapReduce(locator.getShardIterator(), null, (context, obj) -> {
            SelectQuery<EhUserIdentifiersRecord> query = context.selectQuery(Tables.EH_USER_IDENTIFIERS);
            if (callback != null)
                callback.buildCondition(locator, query);

            if (locator.getAnchor() != null)
                query.addConditions(Tables.EH_USER_IDENTIFIERS.ID.lt(locator.getAnchor()));
            if (conditons != null) {
                query.addConditions(conditons);
            }
            query.addOrderBy(Tables.EH_USER_IDENTIFIERS.CREATE_TIME.desc());
            query.addLimit(count - userIdentifiers.size());

            query.fetch().map((r) -> {
                userIdentifiers.add(ConvertHelper.convert(r, UserIdentifier.class));
                return null;
            });

            if (userIdentifiers.size() >= count) {
                locator.setAnchor(userIdentifiers.get(userIdentifiers.size() - 1).getId());
                return AfterAction.done;
            }
            return AfterAction.next;
        });

        if (userIdentifiers.size() > 0) {
            locator.setAnchor(userIdentifiers.get(userIdentifiers.size() - 1).getId());
        }

        return userIdentifiers;
    }

    @Override
    public List<UserIdentifier> listVetsByOrder(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback) {
        // vet number like 1234578743,start with 12
        return listUserIdentifiersByOrder(locator, count, callback,
                Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TOKEN.like("12%"));
    }

    @Override
    public List<UserIdentifier> listAllVerifyCode(CrossShardListingLocator locator, int count,
            ListingQueryBuilderCallback callback) {
        return listUserIdentifiersByOrder(locator, count, callback,
                Tables.EH_USER_IDENTIFIERS.IDENTIFIER_TYPE.eq(IdentifierType.MOBILE.getCode()),Tables.EH_USER_IDENTIFIERS.VERIFICATION_CODE.ne((String)null));
    }

}
