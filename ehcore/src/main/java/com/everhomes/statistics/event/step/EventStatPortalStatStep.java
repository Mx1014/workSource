package com.everhomes.statistics.event.step;

import com.everhomes.db.DbProvider;
import com.everhomes.rest.statistics.event.*;
import com.everhomes.server.schema.tables.EhPortalItemGroups;
import com.everhomes.server.schema.tables.EhPortalLayouts;
import com.everhomes.server.schema.tables.EhPortalNavigationBars;
import com.everhomes.server.schema.tables.EhStatEventPortalConfigs;
import com.everhomes.statistics.event.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by xq.tian on 2017/8/14.
 */
@Component
public class EventStatPortalStatStep extends AbstractStatEventStep {

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private StatEventPortalStatisticProvider statEventPortalStatisticProvider;

    @Autowired
    private StatEventPortalConfigProvider statEventPortalConfigProvider;

    @Autowired
    @Qualifier("PortalLayoutProvider-LaunchPad")
    private PortalLayoutProvider portalLayoutProvider;

    @Autowired
    @Qualifier("PortalNavigationBarProvider-Config")
    private PortalNavigationBarProvider portalNavigationBarProvider;

    @Autowired
    @Qualifier("PortalItemGroupProvider-LaunchPad")
    private PortalItemGroupProvider portalItemGroupProvider;

    @Override
    public void doExecute(StatEventStepExecution execution) {
        LocalDate statDate = execution.getParam("statDate");

        Date date = Date.valueOf(statDate);

        dbProvider.execute(status -> {
            this.bottomNavigationBarStatProcess(date);
            this.topNavigationBarStatProcess(date);
            this.layoutAndItemGroupStatProcess(date);
            return true;
        });
    }

    private void bottomNavigationBarStatProcess(Date date) {
        final byte statusReleased = 4;// 发布状态

        // 拿到已发布的底部导航栏
        List<PortalNavigationBar> navigationBars = portalNavigationBarProvider.listPortalNavigationBarByStatus(statusReleased);
        for (PortalNavigationBar navigationBar : navigationBars) {
            StatEventPortalStatistic stat = new StatEventPortalStatistic();
            stat.setNamespaceId(navigationBar.getNamespaceId());
            stat.setStatDate(date);
            stat.setOwnerType(EhPortalNavigationBars.class.getSimpleName());
            stat.setOwnerId(navigationBar.getId());
            stat.setDisplayName(navigationBar.getLabel());
            stat.setName(navigationBar.getLabel());
            stat.setStatType(StatEventPortalStatType.BOTTOM_NAVIGATION.getCode());
            stat.setDescription(navigationBar.getDescription());
            stat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());
            stat.setStatus(StatEventCommonStatus.ACTIVE.getCode());
            stat.setIdentifier(StatEventPortalIdentifier.BOTTOM_NAVIGATION.getCode());// identifier
            statEventPortalStatisticProvider.createStatEventPortalStatistic(stat);
        }
    }

    private void topNavigationBarStatProcess(Date date) {
        final byte statusReleased = 4;// 发布状态

        // 拿到已发布的顶部工具栏
        List<StatEventPortalConfig> portalConfigs = statEventPortalConfigProvider.listStatEventPortalConfigs(
                StatEventPortalConfigType.TOP_NAVIGATION.getCode(), statusReleased);
        for (StatEventPortalConfig portalConfig : portalConfigs) {
            StatEventPortalStatistic stat = new StatEventPortalStatistic();
            stat.setNamespaceId(portalConfig.getNamespaceId());
            stat.setStatDate(date);
            stat.setOwnerType(EhStatEventPortalConfigs.class.getSimpleName());
            stat.setOwnerId(portalConfig.getId());
            stat.setDisplayName(portalConfig.getDisplayName());
            stat.setName(portalConfig.getConfigName());
            stat.setStatType(StatEventPortalStatType.TOP_NAVIGATION.getCode());
            stat.setDescription(portalConfig.getDescription());
            stat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());
            stat.setStatus(StatEventCommonStatus.ACTIVE.getCode());
            stat.setIdentifier(StatEventPortalIdentifier.TOP_NAVIGATION.getCode());// identifier
            statEventPortalStatisticProvider.createStatEventPortalStatistic(stat);
        }
    }

    private void layoutAndItemGroupStatProcess(Date date) {
        final byte statusReleased = 4;// 发布状态

        // 已发布的门户layout
        List<PortalLayout> layouts = portalLayoutProvider.listPortalLayoutByStatus(statusReleased);
        for (PortalLayout layout : layouts) {
            StatEventPortalStatistic layoutStat = new StatEventPortalStatistic();
            layoutStat.setNamespaceId(layout.getNamespaceId());
            layoutStat.setOwnerType(EhPortalLayouts.class.getSimpleName());
            layoutStat.setOwnerId(layout.getId());
            layoutStat.setDisplayName(layout.getLabel());
            layoutStat.setStatDate(date);
            layoutStat.setStatType(StatEventPortalStatType.PORTAL.getCode());
            layoutStat.setDescription(layout.getDescription());
            layoutStat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());
            layoutStat.setStatus(StatEventCommonStatus.ACTIVE.getCode());
            layoutStat.setIdentifier(layout.getName());// identifier
            statEventPortalStatisticProvider.createStatEventPortalStatistic(layoutStat);

            // 已发布的门户layout item_group
            List<PortalItemGroup> itemGroups = portalItemGroupProvider.listPortalItemGroupByStatus(layout.getId(), statusReleased);
            for (PortalItemGroup itemGroup : itemGroups) {
                StatEventPortalStatistic itemGroupStat = new StatEventPortalStatistic();
                itemGroupStat.setParentId(layoutStat.getId());
                itemGroupStat.setNamespaceId(itemGroup.getNamespaceId());
                itemGroupStat.setOwnerType(EhPortalItemGroups.class.getSimpleName());
                itemGroupStat.setOwnerId(itemGroup.getId());
                itemGroupStat.setDisplayName(itemGroup.getLabel());
                itemGroupStat.setStatDate(date);
                itemGroupStat.setStatType(StatEventPortalStatType.PORTAL_ITEM_GROUP.getCode());
                itemGroupStat.setDescription(itemGroup.getDescription());
                itemGroupStat.setTimeInterval(StatEventStatTimeInterval.DAILY.getCode());
                itemGroupStat.setStatus(StatEventCommonStatus.ACTIVE.getCode());
                itemGroupStat.setIdentifier(String.format("%s:%s:%s", layout.getName(), itemGroup.getWidget(), itemGroup.getName()));// identifier
                statEventPortalStatisticProvider.createStatEventPortalStatistic(itemGroupStat);
            }
        }
    }
}
