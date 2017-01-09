package com.everhomes.launchad;

import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhLaunchAdvertisementsDao;
import com.everhomes.server.schema.tables.pojos.EhLaunchAdvertisements;
import com.everhomes.user.UserContext;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by xq.tian on 2016/12/13.
 */
@Repository
public class LaunchAdProviderImpl implements LaunchAdProvider {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private SequenceProvider sequenceProvider;

    @Override
    public LaunchAd getLaunchAd(Integer namespaceId) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readOnly());
        return context.selectFrom(Tables.EH_LAUNCH_ADVERTISEMENTS)
                .where(Tables.EH_LAUNCH_ADVERTISEMENTS.NAMESPACE_ID.eq(namespaceId))
                // .and(Tables.EH_LAUNCH_ADVERTISEMENTS.STATUS.eq(LaunchAdStatus.ACTIVE.getCode()))
                .fetchAnyInto(LaunchAd.class);
    }

    @Override
    public void updateLaunchAd(LaunchAd launchAd) {
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhLaunchAdvertisementsDao dao = new EhLaunchAdvertisementsDao(context.configuration());
        launchAd.setUpdateUid(UserContext.current().getUser().getId());
        launchAd.setUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
        dao.update(launchAd);
    }

    @Override
    public void createLaunchAd(LaunchAd launchAd) {
        long id = sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhLaunchAdvertisements.class));
        DSLContext context = dbProvider.getDslContext(AccessSpec.readWrite());
        EhLaunchAdvertisementsDao dao = new EhLaunchAdvertisementsDao(context.configuration());
        launchAd.setId(id);
        launchAd.setCreatorUid(UserContext.current().getUser().getId());
        launchAd.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        dao.insert(launchAd);
    }
}
